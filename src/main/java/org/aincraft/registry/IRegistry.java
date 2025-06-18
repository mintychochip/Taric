package org.aincraft.registry;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import org.aincraft.registry.IRegistry.IRegisterable;
import org.jetbrains.annotations.Nullable;

public interface IRegistry<K, T extends IRegisterable<K>> {

  IRegistry<K, T> register(T object);

  @Nullable
  T get(K key);

  boolean isRegistered(K key);

  default Iterator<T> iterator() {
    return values().iterator();
  }

  default Stream<T> stream() {
    return values().stream();
  }

  Collection<T> values();

  interface IRegisterable<K> {

    K getKey();
  }
}
