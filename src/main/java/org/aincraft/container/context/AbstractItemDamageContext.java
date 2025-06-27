package org.aincraft.container.context;

import org.aincraft.api.context.IItemDamageContext;
import org.bukkit.inventory.ItemStack;

abstract class AbstractItemDamageContext<E extends ItemDamageEvent> extends
    AbstractContext<E> implements IItemDamageContext {

  AbstractItemDamageContext(E event) {
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
}
