package org.aincraft.container.gem;

import java.util.function.Consumer;
import org.aincraft.effects.IGemEffect;
import org.bukkit.inventory.ItemStack;

abstract class AbstractEffectContainerHolder<T extends IEffectContainerHolder<V>, V extends IEffectContainerView> implements
    IEffectContainerHolder<V> {

  protected final ItemStack stack;
  protected final IEffectContainer<V> container;

  AbstractEffectContainerHolder(ItemStack stack, IEffectContainer<V> container) {
    this.stack = stack;
    this.container = container;
    container.update(stack);
  }

  @Override
  public void editContainer(Consumer<IEffectContainer<V>> containerConsumer) {
    containerConsumer.accept(container);
    container.update(stack);
  }

  @Override
  public IEffectContainerView getEffectContainerView() {
    return container.getView();
  }

  @Override
  public ItemStack getStack() {
    return stack;
  }

  public abstract IEffectContainerHolder.Builder<T, V> toBuilder();

  abstract static class Builder<T extends IEffectContainerHolder<V>, V extends IEffectContainerView> implements
      IEffectContainerHolder.Builder<T, V> {

    protected final ItemStack stack;
    protected final IEffectContainer<V> container;

    Builder(ItemStack stack, IEffectContainer<V> container) {
      this.stack = stack;
      this.container = container;
    }

    @Override
    public IEffectContainerHolder.Builder<T, V> effect(IGemEffect effect, int rank) {
      container.addEffect(effect, rank);
      return this;
    }

    @Override
    public IEffectContainerHolder.Builder<T, V> effect(IGemEffect effect, int rank, boolean force) {
      container.addEffect(effect, rank, force);
      return null;
    }
  }
}
