package org.aincraft.api.container;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.container.util.WeightedRandom;
import org.aincraft.registry.IRegistry;

public final class Rarities {

  public static IRarity COMMON;
  public static IRarity UNCOMMON;
  public static IRarity RARE;
  public static IRarity EPIC;
  public static IRarity LEGENDARY;
  public static IRarity MYTHIC;

  private static boolean initialized = false;

  public static void initialize(IRegistry<IRarity> registry) {
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

  public static List<IRarity> values() {
    return List.of(COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC);
  }
}
