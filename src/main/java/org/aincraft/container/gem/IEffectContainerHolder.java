package org.aincraft.container.gem;

import java.util.function.Consumer;
import org.aincraft.effects.IGemEffect;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerHolder<View extends IEffectContainerView> {

  void editContainer(Consumer<IEffectContainer<View>> containerConsumer);

  IEffectContainerView getEffectContainerView();

  ItemStack getStack();

  interface Builder<Holder extends IEffectContainerHolder<View>, View extends IEffectContainerView> {

    Builder<Holder, View> effect(IGemEffect effect, int rank);

    Builder<Holder, View> effect(IGemEffect effect, int rank, boolean force);

    Holder build();
  }
}
