package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.context.IEntityDamageEntityContext;
import org.aincraft.api.trigger.IOnEntityHitEntity;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustTransition;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.jetbrains.annotations.NotNull;

public final class Vampirism extends AbstractGemEffect implements IOnEntityHitEntity {

  private static final Particle.DustTransition TRANSITION = new DustTransition(Color.RED,
      Color.MAROON,
      1.5f);

  @Override
  public void onHitEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta) {
    Entity damager = context.getDamager();
    if (!(damager instanceof LivingEntity livingEntity)) {
      return;
    }
    double base = context.getDamage(DamageModifier.BASE);
    double heal = Settings.VAMPIRIC_FACTOR * base;
    double currentHealth = livingEntity.getHealth();

    AttributeInstance attrMaxHealth = livingEntity.getAttribute(Attribute.MAX_HEALTH);
    if (attrMaxHealth == null) {
      return;
    }
    double maxHealth = attrMaxHealth.getValue();

    if (currentHealth + heal <= maxHealth) {
      livingEntity.setHealth(currentHealth + heal);
    } else {
      livingEntity.setHealth(maxHealth);
    }
    if (context.getDamagee() instanceof LivingEntity livingDamagee) {
      playEffects(livingDamagee.getEyeLocation());
    }
  }

  private static void playEffects(@NotNull Location location) {
    World world = location.getWorld();
    if (world == null) {
      return;
    }

    world.spawnParticle(
        Particle.DUST_COLOR_TRANSITION,
        location,
        5,
        0.3, 0.5, 0.3,
        0.1,
        TRANSITION
    );
  }

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_HIT_ENTITY, TargetType.MELEE_WEAPON)
    );
  }
}
