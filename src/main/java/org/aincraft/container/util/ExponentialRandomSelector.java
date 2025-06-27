package org.aincraft.container.util;

import com.google.common.collect.ForwardingList;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.util.IRandomSelector;
import org.jetbrains.annotations.NotNull;

public class ExponentialRandomSelector<T> extends ForwardingList<T> implements IRandomSelector<T> {

  private final List<T> objects = new ArrayList<>();
  private final double base;

  public ExponentialRandomSelector(double base) {
    if (base <= 1.0) {
      throw new IllegalArgumentException("Base must be greater than 1");
    }
    this.base = base;
  }

  @Override
  protected List<T> delegate() {
    return objects;
  }

  @Override
  public @NotNull T getRandom(RandomGenerator randomGenerator) {
    int n = objects.size();
    if (n == 0) {
      throw new IllegalStateException("No elements to select from");
    }

    double totalWeight = 0.0;
    for (int i = 0; i < n; i++) {
      totalWeight += Math.pow(base, -i);
    }

    double r = randomGenerator.nextDouble() * totalWeight;

    double accum = 0.0;
    for (int i = 0; i < n; i++) {
      accum += Math.pow(base, -i);
      if (r < accum) {
        return objects.get(i);
      }
    }

    return objects.get(n - 1);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ExponentialRandomSelector{\n");
    sb.append("  base: ").append(base).append(",\n");
    sb.append("  objects:\n");
    for (int i = 0; i < objects.size(); i++) {
      sb.append("    [").append(i).append("] ").append(objects.get(i)).append("\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
