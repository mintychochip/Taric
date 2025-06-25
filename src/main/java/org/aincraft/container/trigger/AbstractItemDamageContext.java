package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IItemDamageContext;
import org.bukkit.inventory.ItemStack;

abstract class AbstractItemDamageContext<E extends ItemDamageEvent> extends
    AbstractTriggerContext<E> implements IItemDamageContext {

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
