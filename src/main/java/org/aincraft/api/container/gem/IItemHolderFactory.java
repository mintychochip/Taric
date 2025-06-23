package org.aincraft.api.container.gem;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IItemHolderFactory<H extends IItemContainerHolder<C, V>, C extends IItemContainer<V>, V extends IItemContainerView> {

  @NotNull
  H from(ItemStack stack, Callable<? extends H> loader)
      throws IllegalArgumentException, ExecutionException;

  @Nullable
  H fromIfExists(ItemStack stack);

  @Nullable
  C containerIfExists(ItemStack stack);
}
