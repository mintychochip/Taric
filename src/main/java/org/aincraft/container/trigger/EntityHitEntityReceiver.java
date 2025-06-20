package org.aincraft.container.trigger;

import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.container.trigger.IOnEntityHitEntity.IEntityHitEntityReceiver;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public final class EntityHitEntityReceiver extends
    AbstractTriggerReceiver<EntityDamageByEntityEvent> implements
    IEntityHitEntityReceiver {

  @Override
  public Entity getDamager() {
    return event.getDamager();
  }

  @Override
  public Entity getDamagee() {
    return event.getEntity();
  }

  @Override
  public void setDamage(double damage) {
    event.setDamage(damage);
  }

  @Override
  public void setDamage(DamageModifier modifier, double value) {
    event.setDamage(modifier, value);
  }

  @Override
  public double getDamage(DamageModifier modifier) {
    return event.getDamage(modifier);
  }

  @Override
  public double getDamage() {
    return event.getDamage();
  }
}
