package org.aincraft.api.container.trigger;

import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public interface IOnEntityHitEntity {

  void onHitEntity(IEntityHitEntityReceiver receiver);

  interface IEntityHitEntityReceiver extends ITriggerReceiver {

    Entity getDamager();

    Entity getDamagee();

    void setDamage(double damage);

    void setDamage(DamageModifier modifier, double value);

    double getDamage(DamageModifier modifier);

    double getDamage();
  }
}
