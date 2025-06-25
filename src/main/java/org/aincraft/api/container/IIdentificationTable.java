package org.aincraft.api.container;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Keyed;

public interface IIdentificationTable extends Keyed {

  IRarity getRandom(RandomGenerator randomGenerator);
}
