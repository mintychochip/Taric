package org.aincraft.registry;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SharedRegistry<T extends Keyed> implements IRegistry<T> {

  private final Map<Key, T> registry = new ConcurrentHashMap<>();

  @Override
  public synchronized IRegistry<T> register(T object) {
    registry.put(object.key(), object);
    return this;
  }

  @Override
  public T get(Keyed keyed) {
    return get(keyed.key());
  }

  @Override
  public int size() {
    return registry.size();
  }

  @Override
  public @Nullable T get(Key key) {
    return registry.get(key);
  }

  @Override
  public boolean isRegistered(Key key) {
    return registry.containsKey(key);
  }

  @Override
  public boolean isRegistered(T object) {
    return isRegistered(object.key());
  }

  @Override
  public Collection<T> values() {
    return registry.values();
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return registry.values().iterator();
  }
}
