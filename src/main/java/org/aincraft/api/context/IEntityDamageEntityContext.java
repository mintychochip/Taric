package org.aincraft.api.context;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public interface IEntityDamageEntityContext {

  Entity getDamager();

  Entity getDamagee();

  @SuppressWarnings("deprecation")
  void setDamage(DamageModifier modifier, double value);

  @SuppressWarnings("deprecation")
  double getDamage(DamageModifier modifier);

  double getDamage();

  void setDamage(double damage);
}
