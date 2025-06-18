package org.aincraft.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.registry.IRegistry.IRegisterable;

public class SimpleRegistry<K, T extends IRegisterable<K>> implements IRegistry<K, T> {

  private final Map<K, T> registry = new HashMap<>();

  @Override
  public IRegistry<K, T> register(T object) {
    registry.putIfAbsent(object.getKey(), object);
    return this;
  }

  public T get(K key) {
    return registry.get(key);
  }

  @Override
  public boolean isRegistered(K key) {
    return registry.containsKey(key);
  }

  @Override
  public Collection<T> values() {
    return registry.values();
  }
}
