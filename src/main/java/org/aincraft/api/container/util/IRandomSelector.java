package org.aincraft.api.container.util;

import java.util.random.RandomGenerator;
import org.jetbrains.annotations.Nullable;

public interface IRandomSelector<T> {

  @Nullable
  T getRandom(RandomGenerator randomGenerator) throws IllegalStateException;

}
