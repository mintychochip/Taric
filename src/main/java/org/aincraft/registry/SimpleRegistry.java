package org.aincraft.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public class SimpleRegistry<T extends Keyed> implements IRegistry<T> {

  private final Set<T> registry = new HashSet<>();


  @Override
  public IRegistry<T> register(T object) {
    registry.add(object);
    return this;
  }

  @Override
  public @Nullable T get(Key key) {
    return registry.stream().filter(item -> item.key().equals(key)).findFirst().orElse(null);
  }

  @Override
  public boolean isRegistered(Key key) {
    return get(key) != null;
  }

  @Override
  public Collection<T> values() {
    return new ArrayList<>(registry);
  }
}
