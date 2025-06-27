package org.aincraft.container.context;

import java.util.List;
import org.aincraft.api.context.IShearEntityContext;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractShearEntityContext<E extends ShearEntityEvent> extends
    AbstractContext<E> implements
    IShearEntityContext {

  AbstractShearEntityContext(E event) {
    super(event);
  }

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
