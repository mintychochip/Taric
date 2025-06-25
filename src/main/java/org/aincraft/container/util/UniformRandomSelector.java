package org.aincraft.container.util;

import com.google.common.collect.ForwardingList;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.util.IRandomSelector;

public class UniformRandomSelector<T> extends ForwardingList<T> implements IRandomSelector<T> {

  private final List<T> objects = new ArrayList<>();

  @Override
  protected List<T> delegate() {
    return objects;
  }

  @Override
  public T getRandom(RandomGenerator randomGenerator) throws IllegalStateException {
    if (objects.isEmpty()) {
      throw new IllegalStateException("no elements to select from");
    }
    int index = randomGenerator.nextInt(this.objects.size());
    return objects.get(index);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("UniformRandomSelector{\n");
    for (T obj : objects) {
      sb.append("  ").append(obj).append("\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
