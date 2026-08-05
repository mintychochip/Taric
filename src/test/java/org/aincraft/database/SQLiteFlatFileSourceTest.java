package org.aincraft.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Guards the SQLite flat-file source type after the FLat → Flat rename. Compiling
 * {@link SQLiteFlatFileSource} (and its use from {@link StorageProvider}) is the main proof;
 * these asserts lock the corrected simple name and ensure the typo source file does not return.
 */
class SQLiteFlatFileSourceTest {

  private static final Path DATABASE_SOURCES =
      Path.of("src/main/java/org/aincraft/database");

  @Test
  void classSimpleName_isCorrectedSpelling() {
    assertEquals("SQLiteFlatFileSource", SQLiteFlatFileSource.class.getSimpleName());
  }

  @Test
  void typoSourceFile_doesNotExist_andCorrectSourceDoes() {
    assertFalse(Files.exists(DATABASE_SOURCES.resolve("SQLiteFLatFileSource.java")),
        "typo-named source SQLiteFLatFileSource.java must not exist");
    assertTrue(Files.exists(DATABASE_SOURCES.resolve("SQLiteFlatFileSource.java")),
        "SQLiteFlatFileSource.java must exist under database package");
  }
}
