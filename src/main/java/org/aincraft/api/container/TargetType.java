package org.aincraft.api.container;

import org.bukkit.Material;

public final class TargetType {

  public static final TypeSet HELMET = TypeSet.withSuffix("_HELMET");
  public static final TypeSet BOOTS = TypeSet.withSuffix("_BOOTS");
  public static final TypeSet CHESTPLATE = TypeSet.withSuffix("_CHESTPLATE");
  public static final TypeSet LEGGINGS = TypeSet.withSuffix("_LEGGINGS");
  public static final TypeSet AXE = TypeSet.withSuffix("_AXE");
  public static final TypeSet PICKAXE = TypeSet.withSuffix("_PICKAXE");
  public static final TypeSet SHOVEL = TypeSet.withSuffix("_SHOVEL");
  public static final TypeSet HOE = TypeSet.withSuffix("_HOE");
  public static final TypeSet SWORD = TypeSet.withSuffix("_SWORD");
  public static final TypeSet BOW = TypeSet.single(Material.BOW);
  public static final TypeSet CROSSBOW = TypeSet.single(Material.CROSSBOW);
  public static final TypeSet RANGED_WEAPON = TypeSet.builder().union(BOW, CROSSBOW).build();
  public static final TypeSet MELEE_WEAPON = TypeSet.builder().union(AXE, SWORD).build();
  public static final TypeSet WEAPON = TypeSet.builder().union(RANGED_WEAPON, MELEE_WEAPON).build();
  public static final TypeSet ARMOR = TypeSet.builder()
      .union(HELMET, BOOTS, CHESTPLATE, LEGGINGS)
      .build();

  public static final TypeSet TOOL = TypeSet.builder()
      .union(AXE, PICKAXE, SHOVEL, HOE, SWORD)
      .build();
  public static final TypeSet ALL = TypeSet.builder().union(WEAPON,ARMOR,TOOL).build();
}
