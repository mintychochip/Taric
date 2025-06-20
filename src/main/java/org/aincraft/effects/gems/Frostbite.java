package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnEntityHitEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.jetbrains.annotations.NotNull;

final class Frostbite extends AbstractGemEffect implements IOnEntityHitEntity {

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
  public void onHitEntity(IEntityHitEntityReceiver receiver) {
    Entity damagee = receiver.getDamagee();
    int base = damagee.getFreezeTicks();
    damagee.setFreezeTicks(
        Math.min(base + receiver.getRank() * Settings.COLD_ASPECT_FREEZE_TICKS_RANK,
            Settings.COLD_ASPECT_MAX_FREEZE_TICKS));
    if (damagee instanceof LivingEntity livingEntity) {
      playEffects(livingEntity.getEyeLocation());
    }
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.ENTITY_HIT_ENTITY, TargetType.MELEE_WEAPON)
    );
  }
}
