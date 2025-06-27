package org.aincraft.container.util;

import com.google.common.collect.ForwardingMap;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.util.IRandomSelector;
import org.jetbrains.annotations.NotNull;

public class WeightedRandomSelector<T> extends
    ForwardingMap<T, Double> implements
    IRandomSelector<T> {

  private final Map<T, Double> objects = new HashMap<>();

  @Override
  protected Map<T, Double> delegate() {
    return objects;
  }

  @Override
  public @NotNull T getRandom(RandomGenerator randomGenerator) throws IllegalStateException {
    if (objects.isEmpty()) {
      throw new IllegalStateException("objects cannot be empty");
    }
    double cumulativeSum = 0;
    for (Double weight : objects.values()) {
      cumulativeSum += weight;
    }

    double target = randomGenerator.nextDouble() * cumulativeSum;
    double runningSum = 0;
    for (Entry<T, Double> entry : objects.entrySet()) {
      runningSum += entry.getValue();
      if (target < runningSum) {
        return entry.getKey();
      }
    }
    throw new IllegalStateException("Random selection failed despite non-empty, weighted input.");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("WeightedRandomSelector{\n");
    for (Map.Entry<T, Double> entry : objects.entrySet()) {
      sb.append("  ")
          .append(entry.getKey())
          .append(": ")
          .append(entry.getValue())
          .append("\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
