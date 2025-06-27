package org.aincraft.api.container.util;

import java.util.random.RandomGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRandomSelector<T> {

  @NotNull
  T getRandom(RandomGenerator randomGenerator) throws IllegalStateException;

}
