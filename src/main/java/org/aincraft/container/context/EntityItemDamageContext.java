package org.aincraft.container.context;

import org.aincraft.api.context.IItemDamageContext.IEntityItemDamageContext;
import org.aincraft.container.context.ItemDamageEvent.IEntityDamageItemEvent;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

final class EntityItemDamageContext extends
    AbstractContext<IEntityDamageItemEvent> implements
    IEntityItemDamageContext {


  EntityItemDamageContext(IEntityDamageItemEvent event) {
    super(event);
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

  @Override
  public Entity getEntity() {
    return event.getEntity();
  }

}
