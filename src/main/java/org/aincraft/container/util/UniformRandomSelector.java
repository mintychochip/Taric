package org.aincraft.container.util;

import com.google.common.collect.ForwardingList;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.util.IRandomSelector;

class UniformRandomSelector<T> extends ForwardingList<T> implements IRandomSelector<T> {

  private final List<T> objects = new ArrayList<>();

  @Override
  protected List<T> delegate() {
    return objects;
  }

  @Override
  public T getRandom(RandomGenerator randomGenerator) {
    int index = randomGenerator.nextInt(this.objects.size());
    return objects.get(index);
  }
}
