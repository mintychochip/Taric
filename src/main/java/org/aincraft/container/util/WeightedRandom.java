package org.aincraft.container.util;

import com.google.common.collect.ForwardingMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jetbrains.annotations.Nullable;

public class WeightedRandom<T> extends ForwardingMap<T, Double> {

  private final Map<T, Double> objects = new HashMap<>();

  @Override
  protected Map<T, Double> delegate() {
    return objects;
  }

  @Nullable
  public T getRandom(Random random) {
    if (objects.isEmpty()) {
      return null;
    }
    double cumulativeSum = 0;
    for (Double weight : objects.values()) {
      cumulativeSum += weight;
    }

    double target = random.nextDouble() * cumulativeSum;
    double runningSum = 0;
    for (Entry<T, Double> entry : objects.entrySet()) {
      runningSum += entry.getValue();
      if (target < runningSum) {
        return entry.getKey();
      }
    }
    return null;
  }
}
