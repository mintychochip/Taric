package org.aincraft.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.logging.Logger;
import org.aincraft.api.config.IConfiguration;
import org.bukkit.plugin.Plugin;

public final class StorageProvider implements Provider<IDatabase> {

  private final Logger logger;
  private final Plugin plugin;
  private final IConfiguration dbConfiguration;
  private final Extractor extractor;

  @Inject
  public StorageProvider(Logger logger, Plugin plugin, @Named("db") IConfiguration dbConfiguration,
      Extractor extractor) {
    this.logger = logger;
    this.plugin = plugin;
    this.dbConfiguration = dbConfiguration;
    this.extractor = extractor;
  }

  @Override
  public IDatabase get() {
    DatabaseType type = DatabaseType.fromIdentifier(dbConfiguration.getString("type"));
    return switch (type) {
      case H2 ->
          new SQLDatabase(logger, new H2FlatFileSource(logger, plugin.getDataFolder().toPath()),
              extractor);
      case SQLITE -> new SQLDatabase(logger,
          new SQLiteFLatFileSource(logger, plugin.getDataFolder().toPath()), extractor);
      case POSTGRES, MARIA, CASSANDRA -> new SQLDatabase(logger, new HikariConnectionWrapper(type,
          new HikariDataSource(parseConfiguration(dbConfiguration))), extractor);
    };
  }

  private static HikariConfig parseConfiguration(IConfiguration config) {
    String jdbcUrl = config.getString("url");
    if (jdbcUrl == null || jdbcUrl.isBlank()) {
      throw new IllegalArgumentException("Missing or empty JDBC URL ('url' field).");
    }

    String username = config.getString("username");
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Missing or empty database username ('username' field).");
    }

    String password = config.getString("password");
    if (password == null) {
      throw new IllegalArgumentException("Missing database password ('password' field).");
    }

    String type = config.getString("type");
    if (type == null || type.isBlank()) {
      throw new IllegalArgumentException("Missing or empty database type ('type' field).");
    }

    DatabaseType dbType;
    try {
      dbType = DatabaseType.fromIdentifier(type);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid database type: " + type, e);
    }

    int poolSize = config.getInt("pool-size", 10);
    if (poolSize <= 0) {
      throw new IllegalArgumentException("'pool-size' must be a positive integer.");
    }

    int minIdle = config.getInt("min-idle", 2);
    if (minIdle < 0 || minIdle > poolSize) {
      throw new IllegalArgumentException("'min-idle' must be >= 0 and <= pool-size.");
    }

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(jdbcUrl);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    hikariConfig.setMaximumPoolSize(poolSize);
    hikariConfig.setMinimumIdle(minIdle);
    hikariConfig.setDriverClassName(dbType.getClassName());

    return hikariConfig;
  }
}

