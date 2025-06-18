package org.aincraft.database;

public enum DatabaseType {
  H2("h2", "org.h2.Driver"),
  SQLITE("sqlite", "org.sqlite.JDBC"),
  MARIA("mariadb", "org.mariadb.jdbc.Driver"),
  POSTGRES("postgresql", "org.postgresql.Driver"),
  CASSANDRA("cassandra","");

  private final String identifier;
  private final String className;

  DatabaseType(String identifier, String className) {
    this.identifier = identifier;
    this.className = className;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getClassName() {
    return className;
  }

  private static DatabaseType getDefault() {
    return SQLITE;
  }
  public static DatabaseType fromIdentifier(String identifier) {
    for (DatabaseType dbType : DatabaseType.values()) {
      if (dbType.getIdentifier().equals(identifier)) {
        return dbType;
      }
    }
    return DatabaseType.getDefault();
  }
}
