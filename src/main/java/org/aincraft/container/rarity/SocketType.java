package org.aincraft.container.rarity;

import net.kyori.adventure.text.format.TextColor;

final class SocketType {
  private final TextColor color;

  public SocketType(TextColor color) {
    this.color = color;
  }

  public TextColor getColor() {
    return color;
  }
}
