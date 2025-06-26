package org.aincraft.database;

import java.nio.file.Path;
import java.util.logging.Logger;

final class H2FlatFileSource extends FlatFileSource {

  public H2FlatFileSource(Logger logger, Path parentDir) {
    super(logger, parentDir);
  }

  @Override
  public DatabaseType getType() {
    return DatabaseType.H2;
  }
}
