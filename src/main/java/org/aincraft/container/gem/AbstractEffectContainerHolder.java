package org.aincraft.container.gem;

import java.util.function.Consumer;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerHolder;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.inventory.ItemStack;

abstract class AbstractEffectContainerHolder<V extends IEffectContainerView, C extends IEffectContainer<C, V>> implements
    IEffectContainerHolder<V, C> {

  protected final ItemStack stack;
  protected final C container;

  AbstractEffectContainerHolder(ItemStack stack, C container) {
    this.stack = stack;
    this.container = container;
    container.update(stack);
  }

  @Override
  public void editContainer(Consumer<C> containerConsumer) {
    try {
      C clone = container.clone();
      containerConsumer.accept(clone);
      clone.update(stack);
      clone.copy(container, true);
    } catch (Exception e) {
      Taric.getLogger().severe("[Taric] Failed to edit container for item: " + stack.getType());
      Taric.getLogger().severe(e.getMessage());
    }
  }

  @Override
  public V getEffectContainerView() {
    return container.getView();
  }

  @Override
  public ItemStack getStack() {
    return stack;
  }

  abstract static class Builder<H extends IEffectContainerHolder<V, C>, C extends IEffectContainer<C, V>, V extends IEffectContainerView> implements
      IEffectContainerHolder.Builder<H, C, V> {

    protected final ItemStack stack;
    protected final C container;

    Builder(ItemStack stack, C container) {
      this.stack = stack;
      this.container = container;
    }

    @Override
    public IEffectContainerHolder.Builder<H, C, V> effect(IGemEffect effect, int rank) {
      container.addEffect(effect, rank);
      return this;
    }

    @Override
    public IEffectContainerHolder.Builder<H, C, V> effect(IGemEffect effect, int rank,
        boolean force) {
      container.addEffect(effect, rank, force);
      return null;
    }
  }
}
