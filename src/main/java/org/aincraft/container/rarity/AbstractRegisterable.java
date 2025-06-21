package org.aincraft.container.rarity;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

abstract class AbstractRegisterable implements Keyed {

  protected final Key key;

  public AbstractRegisterable(Key key) {
    this.key = key;
  }

  @Override
  public @NotNull Key key() {
    return key;
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Keyed keyed)) {
      return false;
    }
    return this == obj || keyed.key().equals(key());
  }
}
