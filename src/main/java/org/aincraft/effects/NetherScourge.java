package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnEntityHitEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

final class NetherScourge extends AbstractGemEffect implements IOnEntityHitEntity {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.ENTITY_HIT_ENTITY, TargetType.MELEE_WEAPON)
    );
  }

  @Override
  public void onHitEntity(IEntityDamageEntityContext context, int rank) {
    Entity damagee = context.getDamagee();
    if (!Settings.NETHER_SCOURGE_AFFECTED_TYPES.contains(damagee.getType())) {
      return;
    }
    double base = context.getDamage(DamageModifier.BASE);
    double total = base + rank * Taric.getRandom()
        .nextInt(Settings.NETHER_SCOURGE_DAMAGE_RANK_MIN, Settings.NETHER_SCOURGE_DAMAGE_RANK_MAX);
    context.setDamage(DamageModifier.BASE, total);
  }
}
