package org.aincraft.database.hikari;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.aincraft.database.DatabaseType;
import org.aincraft.database.IConnectionSource;

public final class HikariConnectionWrapper implements IConnectionSource {

  private final DatabaseType databaseType;
  private final HikariDataSource dataSource;

  public HikariConnectionWrapper(DatabaseType databaseType, HikariDataSource dataSource) {
    this.databaseType = databaseType;
    this.dataSource = dataSource;
  }

  @Override
  public DatabaseType getType() {
    return databaseType;
  }

  @Override
  public void shutdown() throws SQLException {
    if (dataSource.isClosed()) {
      return;
    }
    dataSource.close();
  }

  @Override
  public boolean isClosed() {
    return dataSource.isClosed();
  }

  @Override
  public Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
