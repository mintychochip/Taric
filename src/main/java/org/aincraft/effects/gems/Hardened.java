package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnEntityHitByEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

final class Hardened extends AbstractGemEffect implements IOnEntityHitByEntity {

  @Override
  public void onHitByEntity(IEntityDamageEntityContext context) {
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

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.ENTITY_HIT_BY_ENTITY, TargetType.PLAYER_ARMOR)
    );
  }
}
