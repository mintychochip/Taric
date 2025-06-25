package org.aincraft.container.trigger;

import java.util.List;
import org.aincraft.api.container.trigger.IShearEntityContext;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractShearEntityContext<E extends ShearEntityEvent> extends
    AbstractTriggerContext<E> implements
    IShearEntityContext {

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
  public @NotNull List<ItemStack> getDrops() {
    return event.getDrops();
  }
}
