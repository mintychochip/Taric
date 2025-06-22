package org.aincraft.api.container;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.format.TextColor;

public interface ISocketColor extends Keyed {
  TextColor getTextColor();
  String getName();
}
