package org.aincraft.container.trigger;

import java.util.List;
import org.aincraft.api.container.trigger.IShearEntityReceiver;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class ShearEntityReceiver<E extends ShearEntityEvent<H>, H extends Event> extends
    AbstractTriggerReceiver<E> implements
    IShearEntityReceiver {

  @Override
  public @NotNull ItemStack getTool() {
    return event.getTool();
  }

  @Override
  public @NotNull Entity getSheared() {
    return event.getSheared();
  }

  @Override
  public void setDrops(List<ItemStack> drops) {
    event.setDrops(drops);
  }

  @Override
  public List<ItemStack> getDrops() {
    return event.getDrops();
  }
}
