package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.context.EntityDamageEntityContext;
import org.aincraft.api.trigger.IOnEntityHitEntity;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

final class NetherScourge extends AbstractGemEffect implements IOnEntityHitEntity {

  @Override
  protected Map<TriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_HIT_ENTITY, TargetType.MELEE_WEAPON)
    );
  }

  @Override
  public void onHitEntity(EntityDamageEntityContext context, EffectInstanceMeta meta) {
    Entity damagee = context.getEntity();
    if (!Settings.NETHER_SCOURGE_AFFECTED_TYPES.contains(damagee.getType())) {
      return;
    }
    double base = context.getDamage(DamageModifier.BASE);
    double total = base + meta.getRank() * Taric.getRandom()
        .nextInt(Settings.NETHER_SCOURGE_DAMAGE_RANK_MIN, Settings.NETHER_SCOURGE_DAMAGE_RANK_MAX);
    context.setDamage(DamageModifier.BASE, total);
  }
}
