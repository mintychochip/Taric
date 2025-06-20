package org.aincraft.api.container;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;

public enum Rarities {
  COMMON,
  UNCOMMON,
  RARE,
  EPIC,
  LEGENDARY,
  MYTHIC;
  private final Key key;

  Rarities() {
    this.key = new NamespacedKey("taric", this.getIdentifier());
  }

  public Key getKey() {
    return key;
  }

  public String getIdentifier() {
    return this.toString().toLowerCase();
  }

}
