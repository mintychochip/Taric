package org.aincraft.container;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;

public enum Rarity {
  COMMON(NamedTextColor.GRAY),
  UNCOMMON(NamedTextColor.GREEN),
  RARE(NamedTextColor.BLUE),
  EPIC(NamedTextColor.DARK_PURPLE),
  LEGENDARY(NamedTextColor.GOLD),
  MYTHIC(NamedTextColor.RED);

  private final RGBLike color;

  Rarity(RGBLike color) {
    this.color = color;
  }

  public RGBLike getColor() {
    return color;
  }
}
