package org.aincraft.api.container;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public enum SocketColors implements Keyed {
  RED,
  ORANGE,
  YELLOW,
  GREEN,
  BLUE,
  PURPLE;

  private final Key key;

  SocketColors() {
    this.key = new NamespacedKey("taric", this.toString().toLowerCase());
  }

  @Override
  public @NotNull Key key() {
    return key;
  }
}
