package org.aincraft.container.util;

import com.google.common.collect.ForwardingMap;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.util.IRandomSelector;

class WeightedRandomSelector<T> extends
    ForwardingMap<T, Double> implements
    IRandomSelector<T> {

  private final Map<T, Double> objects = new HashMap<>();

  @Override
  protected Map<T, Double> delegate() {
    return objects;
  }

  @Override
  public T getRandom(RandomGenerator randomGenerator) {
    if (objects.isEmpty()) {
      return null;
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
    return null;
  }
}
