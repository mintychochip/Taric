package org.aincraft.registry;

import java.util.Collection;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public interface IRegistry<T extends Keyed> extends Iterable<T> {

  interface IRegisterable<K> {

    Key getKey();
  }

  IRegistry<T> register(T object);

  T get(Keyed keyed);

  @Nullable
  T get(Key key);

  T get(int index);

  int size();

  boolean isRegistered(Key key);

  boolean isRegistered(T object);

  default Stream<T> stream() {
    return values().stream();
  }

  Collection<T> values();
}
