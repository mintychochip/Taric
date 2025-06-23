package org.aincraft.api.container.trigger;

import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public interface IOnEntityHitEntity {

  void onHitEntity(IEntityHitEntityContext context);

  interface IEntityHitEntityContext extends ITriggerContext {

    Entity getDamager();

    Entity getDamagee();

    void setDamage(double damage);

    void setDamage(DamageModifier modifier, double value);

    double getDamage(DamageModifier modifier);

    double getDamage();
  }
}
