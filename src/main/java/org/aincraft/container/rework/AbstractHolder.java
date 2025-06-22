package org.aincraft.container.rework;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;

abstract class AbstractHolder<C extends IEffectContainer<V>, V extends IEffectContainerView> implements
    IEffectContainerHolder<C, V> {

  protected final ItemStack stack;
  protected final C container;

  public AbstractHolder(ItemStack stack, C container) {
    this.stack = stack;
    this.container = container;
    container.getView().update(stack);
  }

  @Override
  public ItemStack getStack() {
    return stack;
  }

  @Override
  public V getEffectContainer() {
    return container.getView();
  }

  @Override
  public void editContainer(Consumer<C> containerConsumer) {
    containerConsumer.accept(container);
    container.getView().update(stack);
  }
}
