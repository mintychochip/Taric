package org.aincraft.api.container;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface IIdentificationTable extends Keyed {

  @NotNull
  IRarity getRandom(RandomGenerator randomGenerator);
}
