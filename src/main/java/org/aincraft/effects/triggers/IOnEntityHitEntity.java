package org.aincraft.effects.triggers;

import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public interface IOnEntityHitEntity {
  void onHitEntity(int rank, Entity damager, Entity damagee, Map<DamageModifier, Double> modifiers);
}
