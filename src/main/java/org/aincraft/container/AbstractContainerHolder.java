package org.aincraft.container;

import java.util.function.Consumer;
import org.aincraft.api.container.gem.IContainerHolder;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractContainerHolder<C extends IEffectContainer<V>, V extends IEffectContainerView> implements
    IContainerHolder<C, V> {

  protected final ItemStack stack;

  protected final C container;

  public AbstractContainerHolder(ItemStack stack, C container) {
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
