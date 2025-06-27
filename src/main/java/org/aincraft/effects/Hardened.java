package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.context.IEntityDamageEntityContext;
import org.aincraft.api.trigger.IOnEntityHitByEntity;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

final class Hardened extends AbstractGemEffect implements IOnEntityHitByEntity {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_HIT_BY_ENTITY, TargetType.PLAYER_ARMOR)
    );
  }

  @Override
  public void onHitByEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta) {
    Entity damagee = context.getDamagee();
    if (damagee instanceof LivingEntity) {
      LivingEntity living = (LivingEntity) damagee;

      // Give Resistance I for 5 seconds (100 ticks)
      living.addPotionEffect(new PotionEffect(
          PotionEffectType.RESISTANCE,
          100,  // duration in ticks (5 seconds)
          0     // amplifier (0 = Resistance I)
      ));
    }
  }
}
