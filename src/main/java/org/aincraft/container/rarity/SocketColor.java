package org.aincraft.container.rarity;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.aincraft.api.container.ISocketColor;
import org.jetbrains.annotations.NotNull;

final class SocketColor extends AbstractRegisterable implements ISocketColor {

  private final String name;
  private final TextColor textColor;

  public SocketColor(Key key, String name, TextColor color) {
    super(key);
    this.name = name;
    this.textColor = color;
  }

  @Override
  public TextColor getTextColor() {
    return textColor;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public @NotNull Key key() {
    return key;
  }

  @Override
  public String toString() {
    return key.toString();
  }
}
