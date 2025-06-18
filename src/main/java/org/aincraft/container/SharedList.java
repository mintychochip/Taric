package org.aincraft.container;

import com.google.common.collect.ForwardingList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class SharedList<T> extends ForwardingList<T> {

  private final List<T> internal = new ArrayList<>();

  @Override
  protected List<T> delegate() {
    return internal;
  }

  public void replaceAll(Collection<? extends T> other) {
    internal.clear();
    internal.addAll(other);
  }

  public void replaceAll(Stream<? extends T> other) {
    internal.clear();
    other.forEach(internal::add);
  }
}
