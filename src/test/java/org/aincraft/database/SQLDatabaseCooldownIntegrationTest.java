package org.aincraft.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import net.kyori.adventure.key.Key;
import org.aincraft.container.EffectCooldown;
import org.aincraft.effects.IGemEffect;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration test: real H2 flat-file source, shipped {@code sql/h2.sql} schema via
 * {@link Extractor.ResourceExtractor}, and cooldown CRUD through {@link SQLDatabase}.
 * Player / IGemEffect are mocks; JDBC path is real.
 */
class SQLDatabaseCooldownIntegrationTest {

  @TempDir
  Path tempDir;

  private SQLDatabase database;
  private Player player;
  private IGemEffect effect;
  private final UUID playerId = UUID.fromString("11111111-2222-3333-4444-555555555555");

  @BeforeEach
  void setUp() {
    Logger logger = Logger.getLogger("SQLDatabaseCooldownIntegrationTest");
    H2FlatFileSource source = new H2FlatFileSource(logger, tempDir);
    database = new SQLDatabase(logger, source, new Extractor.ResourceExtractor());

    player = mock(Player.class);
    when(player.getUniqueId()).thenReturn(playerId);

    effect = mock(IGemEffect.class);
    when(effect.key()).thenReturn(Key.key("taric", "blink"));
  }

  @AfterEach
  void tearDown() throws Exception {
    if (database != null) {
      database.shutdown();
    }
  }

  @Test
  void cooldownCreateReadUpdateHasList_endToEnd() {
    assertFalse(database.hasCooldown(player, effect));

    EffectCooldown created = database.createCooldown(player, effect);
    assertNotNull(created);
    assertEquals(playerId, created.getUuid());
    assertEquals("blink", created.getEffect());
    assertNotNull(created.getLastUsed());

    assertTrue(database.hasCooldown(player, effect));

    EffectCooldown loaded = database.getCooldown(player, effect);
    assertNotNull(loaded);
    assertEquals(playerId, loaded.getUuid());
    assertEquals("blink", loaded.getEffect());
    // H2 may truncate sub-millisecond precision when persisting TIMESTAMP values
    assertEquals(created.getLastUsed().getTime(), loaded.getLastUsed().getTime());

    List<EffectCooldown> playerCooldowns = database.getPlayerCooldowns(player);
    assertEquals(1, playerCooldowns.size());
    assertEquals("blink", playerCooldowns.get(0).getEffect());

    // Allow clock to advance so updated timestamp differs when compared at second precision
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    assertTrue(database.updateCooldown(player, effect));

    EffectCooldown updated = database.getCooldown(player, effect);
    assertNotNull(updated);
    assertFalse(updated.getLastUsed().before(created.getLastUsed()));

    boolean hasDbArtifact =
        Files.exists(tempDir.resolve("taric-h2.db"))
            || Files.exists(tempDir.resolve("taric-h2.db.mv.db"))
            || Files.exists(tempDir.resolve("taric-h2.mv.db"));
    assertTrue(hasDbArtifact, "H2 data files should exist under the temp data directory");
  }
}
