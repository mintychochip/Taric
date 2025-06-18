package org.aincraft.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionSource {
  DatabaseType getType();
  void shutdown() throws SQLException;
  boolean isClosed();
  Connection getConnection();
}
