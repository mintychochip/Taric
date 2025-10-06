package org.aincraft.api.container;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Keyed;
import org.aincraft.api.Rarity;
import org.jetbrains.annotations.NotNull;

public interface IIdentificationTable extends Keyed {

  @NotNull
  Rarity getRandom(RandomGenerator randomGenerator);
}
