package org.aincraft.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.aincraft.container.EffectCooldown;
import org.aincraft.effects.IGemEffect;
import org.bukkit.entity.Player;

public final class SQLDatabase implements IDatabase {

  private static final String CREATE_COOLDOWN = """
      INSERT INTO taric_cooldowns (id,effect,last_used) VALUES (?,?,?);
      """;

  private static final String GET_COOLDOWN = """
      SELECT last_used FROM taric_cooldowns WHERE id=? AND effect=?;
      """;

  private static final String UPDATE_COOLDOWN = """
      UPDATE taric_cooldowns SET last_used=? WHERE id=? AND effect=?;
      """;

  private static final String HAS_COOLDOWN = """
      SELECT EXISTS (SELECT 1 from taric_cooldowns WHERE id=? AND effect=?);
      """;

  private static final String GET_PLAYER_COOLDOWNS = """
      SELECT effect,last_used from taric_cooldowns WHERE id=?;
      """;

  private final Logger logger;
  private final IConnectionSource source;
  private final SQLExecutor executor;
  private final Extractor extractor;

  public SQLDatabase(Logger logger, IConnectionSource source, Extractor extractor) {
    this.logger = logger;
    this.source = source;
    this.extractor = extractor;
    this.executor = new SQLExecutor(source, logger);
    if (!isSetup()) {
      String[] tables = this.getSqlTables().toArray(new String[0]);
      try {
        executor.executeBulk(tables);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      logger.info("Successfully added tables to the database");
    }
  }

  private boolean isSetup() {
    String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name LIKE '%matcha%'";
    if (source.getType() == DatabaseType.SQLITE) {
      query = "SELECT 1 FROM sqlite_master WHERE type='table' LIMIT 1";
    }
    try (Connection connection = source.getConnection()) {
      PreparedStatement ps = connection.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getSqlTables() {
    try (InputStream resourceStream = extractor.getResourceStream(
        "sql/%s.sql".formatted(source.getType().getIdentifier()))) {
      String tables = new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8);
      return Arrays.stream(tables.split(";"))
          .map(s -> s.trim() + ";")
          .filter(s -> !s.equals(";"))
          .toList();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public EffectCooldown createCooldown(Player player, IGemEffect effect) {
    Object uuid = getDatabaseUUID(player.getUniqueId(), source.getType());
    String effectKey = effect.getKey();
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    executor.executeUpdate(CREATE_COOLDOWN, uuid, effectKey, now);
    return new EffectCooldown(player.getUniqueId(), effectKey, now);
  }

  @Override
  public EffectCooldown getCooldown(Player player, IGemEffect effect) {
    Object uuid = getDatabaseUUID(player.getUniqueId(), source.getType());
    String effectKey = effect.getKey();
    return executor.queryRow(scanner -> {
      try {
        Timestamp lastUsed = scanner.getTimestamp("last_used");
        return new EffectCooldown(player.getUniqueId(), effectKey, lastUsed);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }, GET_COOLDOWN, uuid, effectKey);
  }

  @Override
  public boolean updateCooldown(Player player, IGemEffect effect) {
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    Object uuid = getDatabaseUUID(player.getUniqueId(), source.getType());
    String effectKey = effect.getKey();
    return executor.executeUpdate(UPDATE_COOLDOWN, now, uuid, effectKey);
  }

  @Override
  public boolean hasCooldown(Player player, IGemEffect effect) {
    Object uuid = getDatabaseUUID(player.getUniqueId(), source.getType());
    String effectKey = effect.getKey();
    return executor.queryRow(scanner -> {
      try {
        return scanner.getBoolean(1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }, HAS_COOLDOWN, uuid, effectKey);
  }

  @Override
  public List<EffectCooldown> getPlayerCooldowns(Player player) {
    Object uuid = getDatabaseUUID(player.getUniqueId(), source.getType());
    return executor.queryTable(scanner -> {
      try {
        String effectKey = scanner.getString("effect");
        Timestamp lastUsed = scanner.getTimestamp("last_used");
        return new EffectCooldown(player.getUniqueId(), effectKey, lastUsed);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }, GET_PLAYER_COOLDOWNS, uuid);
  }

  @Override
  public void shutdown() throws SQLException {
    source.shutdown();
  }

  private static Object getDatabaseUUID(UUID uuid, DatabaseType type) {
    if (type == DatabaseType.MARIA || type == DatabaseType.POSTGRES
        || type == DatabaseType.CASSANDRA) {
      return uuid;
    }
    return uuid.toString();
  }
}
