package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IOnEntityItemDamage.IEntityDamageItemContext;
import org.aincraft.container.trigger.ItemDamageEvent.IEntityDamageItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

final class EntityDamageItemContext extends
    AbstractContext<IEntityDamageItemEvent> implements
    IEntityDamageItemContext {


  EntityDamageItemContext(IEntityDamageItemEvent event) {
    super(event);
  }

  @Override
  public double getDamage() {
    return event.getDamage();
  }

  @Override
  public void setDamage(int damage) {
    event.setDamage(damage);
  }

  @Override
  public ItemStack getItem() {
    return event.getItem();
  }

  @Override
  public Entity getEntity() {
    return event.getEntity();
  }

  @Override
  public EntityType getEntityType() {
    return event.getEntityType();
  }
}
