package org.aincraft.container.context;

import org.aincraft.api.context.IEntityDamageEntityContext;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

final class EntityDamageEntityContext extends
    AbstractContext<EntityDamageByEntityEvent> implements
    IEntityDamageEntityContext {

  EntityDamageEntityContext(EntityDamageByEntityEvent event) {
    super(event);
  }

  @Override
  public Entity getDamager() {
    return event.getDamager();
  }

  @Override
  public Entity getDamagee() {
    return event.getEntity();
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

  @Override
  public void setDamage(double damage) {
    event.setDamage(damage);
  }
}
