package org.aincraft.container;

import io.papermc.paper.persistence.PersistentDataContainerView;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IContainerHolder;
import org.aincraft.api.container.gem.IContainerHolderFactory;
import org.aincraft.api.container.gem.IItemContainer;
import org.aincraft.api.container.gem.IItemContainerView;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class ContainerHolderFactory<H extends IContainerHolder<C, V>, C extends IItemContainer<V>, V extends IItemContainerView> implements
    IContainerHolderFactory<H, C, V> {

  @Override
  public @NotNull H from(ItemStack stack, Callable<? extends H> loader)
      throws IllegalArgumentException, ExecutionException {
    Material material = stack.getType();
    if (material.isAir()) {
      throw new IllegalArgumentException("stack cannot be air");
    }
    if (!hasContainer(this.getContainerKey(), stack)) {
      try {
        return loader.call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }

    C container = containerIfExists(stack);
    if (container == null) {
      throw new IllegalArgumentException("container not found, this is a bug");
    }
    return create(stack, container);
  }

  static boolean hasContainer(NamespacedKey key, ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return false;
    }
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.has(key);
  }

  protected abstract NamespacedKey getContainerKey();

  protected abstract H create(ItemStack stack, C container);

  protected abstract Class<? extends C> getContainerImplClazz();

  @Override
  public @Nullable H fromIfExists(ItemStack stack) {
    C container = containerIfExists(stack);
    return container != null ? create(stack, container) : null;
  }

  @Override
  public @Nullable C containerIfExists(ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir() || !hasContainer(getContainerKey(), stack)) {
      return null;
    }
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    String json = pdc.get(getContainerKey(), PersistentDataType.STRING);
    return Taric.getGson().fromJson(json, getContainerImplClazz());
  }
}
