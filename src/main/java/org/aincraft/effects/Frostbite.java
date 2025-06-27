package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnEntityHitEntity;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.container.registerable.TriggerTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

final class Frostbite extends AbstractGemEffect implements IOnEntityHitEntity {

  @Override
  public void onHitEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta) {
    Entity damagee = context.getDamagee();
    int base = damagee.getFreezeTicks();
    damagee.setFreezeTicks(
        Math.min(base + meta.getRank() * Settings.COLD_ASPECT_FREEZE_TICKS_RANK,
            Settings.COLD_ASPECT_MAX_FREEZE_TICKS));
    if (damagee instanceof LivingEntity livingEntity) {
      playEffects(livingEntity.getEyeLocation());
    }
  }

  private static void playEffects(@NotNull Location location) {
    World world = location.getWorld();
    if (world == null) {
      return;
    }

    world.spawnParticle(
        Particle.SNOWFLAKE,
        location,
        10,
        0.3, 0.5, 0.3,
        0.1
    );
  }

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_HIT_ENTITY, TargetType.MELEE_WEAPON)
    );
  }
}
