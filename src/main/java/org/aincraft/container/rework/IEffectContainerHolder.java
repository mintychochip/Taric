package org.aincraft.container.rework;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerHolder<C extends IEffectContainer<V>, V extends IEffectContainerView> {

  ItemStack getStack();

  V getEffectContainer();

  void editContainer(Consumer<C> containerConsumer);
}
