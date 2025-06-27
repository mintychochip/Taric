package org.aincraft.container.context;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.aincraft.container.context.ItemDamageEvent.IEntityDamageItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

final class EntityItemDamageEventDecorator implements IEntityDamageItemEvent {

  private final EntityDamageItemEvent event;

  EntityItemDamageEventDecorator(EntityDamageItemEvent event) {
    this.event = event;
  }

  @Override
  public Entity getEntity() {
    return event.getEntity();
  }

  @Override
  public EntityType getEntityType() {
    return event.getEntityType();
  }

  @Override
  public ItemStack getItem() {
    return event.getItem();
  }

  @Override
  public int getDamage() {
    return event.getDamage();
  }

  @Override
  public void setDamage(int damage) {
    event.setDamage(damage);
  }
}
