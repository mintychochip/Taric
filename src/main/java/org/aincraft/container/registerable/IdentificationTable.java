package org.aincraft.container.registerable;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Key;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.util.IRandomSelector;
import org.jetbrains.annotations.NotNull;

final class IdentificationTable extends AbstractRegisterable implements IIdentificationTable {

  private final IRandomSelector<IRarity> randomSelector;

  public IdentificationTable(Key key, IRandomSelector<IRarity> randomSelector) {
    super(key);
    this.randomSelector = randomSelector;
  }

  @Override
  public @NotNull IRarity getRandom(RandomGenerator randomGenerator) {
    return randomSelector.getRandom(randomGenerator);
  }

  @Override
  public String toString() {
    return "IdentificationTable{" +
        "key=" + key +
        ", randomSelector=" + randomSelector +
        '}';
  }
}
