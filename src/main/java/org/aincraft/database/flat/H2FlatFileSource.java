package org.aincraft.database.flat;

import java.nio.file.Path;
import java.util.logging.Logger;
import org.aincraft.database.DatabaseType;

public class H2FlatFileSource extends FlatFileSource {

  public H2FlatFileSource(Logger logger, Path parentDir) {
    super(logger, parentDir);
  }

  @Override
  public DatabaseType getType() {
    return DatabaseType.H2;
  }
}
