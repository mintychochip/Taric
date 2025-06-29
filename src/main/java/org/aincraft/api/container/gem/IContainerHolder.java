package org.aincraft.api.container.gem;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IContainerHolder<C extends IEffectContainer<V>, V extends IEffectContainerView> {

  @NotNull
  ItemStack getStack();

  V getContainer();

  void editContainer(Consumer<C> containerConsumer);

}
