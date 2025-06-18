package org.aincraft.effects.gems;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.container.Mutable;
import org.aincraft.container.TargetType;
import org.aincraft.effects.triggers.IOnKillEntity;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class Vorpal extends AbstractGemEffect implements IOnKillEntity {

  @Override
  public void onKillEntity(int rank, DamageSource damageSource, LivingEntity entity,
      Mutable<Integer> experience, List<ItemStack> drops) {
    EntityType type = entity.getType();
    if (!(type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.CREEPER
        || type == EntityType.WITHER_SKELETON || type == EntityType.PIGLIN)) {
      return;
    }
    if (entity.getType() == EntityType.WITHER_SKELETON) {
      for (ItemStack drop : drops) {
        Material material = drop.getType();
        String materialString = material.toString();
        if (materialString.endsWith("_HEAD") || materialString.endsWith("_SKULL")) {
          return;
        }
      }
    }
    if (Taric.getRandom().nextDouble() <= Settings.VORPAL_CHANCE_RANK * rank) {
      Material material = getHeadFromType(type);
      if (material == null) {
        return;
      }
      drops.add(new ItemStack(material));
    }
  }

  @Nullable
  private static Material getHeadFromType(EntityType type) {
    if (!(type == EntityType.SKELETON || type == EntityType.WITHER_SKELETON)) {
      try {
        return Material.valueOf(type.toString() + "_HEAD");
      } catch (IllegalArgumentException ex) {
        return null;
      }
    }
    return type == EntityType.SKELETON ? Material.SKELETON_SKULL : Material.WITHER_SKELETON_SKULL;
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.KILL_ENTITY, TargetType.MELEE_WEAPON)
    );
  }
}
