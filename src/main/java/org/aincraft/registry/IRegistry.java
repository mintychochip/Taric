package org.aincraft.registry;

import java.util.Collection;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public interface IRegistry<T extends Keyed> extends Iterable<T> {

  IRegistry<T> register(T object);

  @Nullable
  T get(Key key);

  boolean isRegistered(Key key);

  default Stream<T> stream() {
    return values().stream();
  }

  Collection<T> values();

  interface IRegisterable<K> {

    Key getKey();
  }
}
