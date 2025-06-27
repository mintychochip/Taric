package org.aincraft.container.context;

import org.aincraft.api.context.IEntityItemDamageContext;
import org.aincraft.container.context.ItemDamageEvent.IEntityDamageItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

final class EntityItemDamageContext extends
    AbstractContext<IEntityDamageItemEvent> implements
    IEntityItemDamageContext {


  EntityItemDamageContext(IEntityDamageItemEvent event) {
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
