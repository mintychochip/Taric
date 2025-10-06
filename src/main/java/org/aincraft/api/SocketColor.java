package org.aincraft.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public record SocketColor(Key key, Component name, TextColor textColor) implements Keyed {

  @Override
  public String toString() {
    return key.toString();
  }
}
