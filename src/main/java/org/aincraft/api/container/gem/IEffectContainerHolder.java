package org.aincraft.api.container.gem;

import java.util.function.Consumer;
import org.aincraft.effects.IGemEffect;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerHolder<V extends IEffectContainerView, C extends IEffectContainer<C, V>> {

  void editContainer(Consumer<C> containerConsumer);

  V getEffectContainerView();

  ItemStack getStack();

  interface Builder<H extends IEffectContainerHolder<V, C>, C extends IEffectContainer<C, V>, V extends IEffectContainerView> {

    Builder<H, C, V> effect(IGemEffect effect, int rank);

    Builder<H, C, V> effect(IGemEffect effect, int rank, boolean force);

    H build();
  }
}
