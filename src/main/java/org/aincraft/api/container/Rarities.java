package org.aincraft.api.container;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.api.Rarity;
import org.aincraft.registry.IRegistry;

public final class Rarities {

  public static Rarity COMMON;
  public static Rarity UNCOMMON;
  public static Rarity RARE;
  public static Rarity EPIC;
  public static Rarity LEGENDARY;
  public static Rarity MYTHIC;

  private static boolean initialized = false;

  public static void initialize(IRegistry<Rarity> registry) {
    if (initialized) {
      throw new IllegalStateException("rarities already initialized");
    }
    initialized = true;
    COMMON = registry.get(Key.key("taric:common"));
    UNCOMMON = registry.get(Key.key("taric:uncommon"));
    RARE = registry.get(Key.key("taric:rare"));
    EPIC = registry.get(Key.key("taric:epic"));
    LEGENDARY = registry.get(Key.key("taric:legendary"));
    MYTHIC = registry.get(Key.key("taric:mythic"));
  }

  public static List<Rarity> values() {
    return List.of(COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC);
  }
}
