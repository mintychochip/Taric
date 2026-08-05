package org.aincraft.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DatabaseTypeTest {

  @ParameterizedTest
  @CsvSource({
      "h2, H2",
      "sqlite, SQLITE",
      "mariadb, MARIA",
      "postgresql, POSTGRES",
      "cassandra, CASSANDRA"
  })
  void fromIdentifier_knownIdentifiers(String identifier, DatabaseType expected) {
    assertEquals(expected, DatabaseType.fromIdentifier(identifier));
  }

  @Test
  void fromIdentifier_unknown_defaultsToSqlite() {
    assertEquals(DatabaseType.SQLITE, DatabaseType.fromIdentifier("unknown"));
    assertEquals(DatabaseType.SQLITE, DatabaseType.fromIdentifier(""));
    assertEquals(DatabaseType.SQLITE, DatabaseType.fromIdentifier("H2"));
  }

  @Test
  void getIdentifier_and_getClassName() {
    assertEquals("h2", DatabaseType.H2.getIdentifier());
    assertEquals("org.h2.Driver", DatabaseType.H2.getClassName());
    assertEquals("sqlite", DatabaseType.SQLITE.getIdentifier());
    assertEquals("org.sqlite.JDBC", DatabaseType.SQLITE.getClassName());
  }
}
