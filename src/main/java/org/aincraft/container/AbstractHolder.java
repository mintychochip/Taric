package org.aincraft.container;

import java.util.function.Consumer;
import org.aincraft.api.container.gem.IItemContainer;
import org.aincraft.api.container.gem.IItemContainerHolder;
import org.aincraft.api.container.gem.IItemContainerView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractHolder<C extends IItemContainer<V>, V extends IItemContainerView> implements
    IItemContainerHolder<C, V> {

  protected final ItemStack stack;

  protected final C container;

  public AbstractHolder(ItemStack stack, C container) {
    this.stack = stack;
    this.container = container;
    container.getView().update(stack);
  }

  @Override
  public @NotNull ItemStack getStack() {
    return stack;
  }

  @Override
  public V getContainer() {
    return container.getView();
  }

  @Override
  public void editContainer(Consumer<C> containerConsumer) {
    containerConsumer.accept(container);
    container.getView().update(stack);
  }
}
