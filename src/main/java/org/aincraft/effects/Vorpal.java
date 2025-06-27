package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnEntityKill;
import org.aincraft.container.registerable.TriggerTypes;
import org.aincraft.container.registerable.ITriggerType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class Vorpal extends AbstractGemEffect implements IOnEntityKill {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_KILL, TargetType.MELEE_WEAPON)
    );
  }

  @Override
  public void onKillEntity(IEntityKillContext context, EffectInstanceMeta meta) {
    EntityType type = context.getSlain().getType();
    if (!(type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.CREEPER
        || type == EntityType.WITHER_SKELETON || type == EntityType.PIGLIN)) {
      return;
    }
    if (type == EntityType.WITHER_SKELETON) {
      for (ItemStack drop : context.getDrops()) {
        Material material = drop.getType();
        String materialString = material.toString();
        if (materialString.endsWith("_HEAD") || materialString.endsWith("_SKULL")) {
          return;
        }
      }
    }
    if (Taric.getRandom().nextDouble() <= Settings.VORPAL_CHANCE_RANK * meta.getRank()) {
      Material material = getHeadFromType(type);
      if (material == null) {
        return;
      }
      context.getDrops().add(new ItemStack(material));
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
}
