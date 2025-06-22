package org.aincraft.container.rework;

import io.papermc.paper.persistence.PersistentDataContainerView;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.aincraft.Taric;
import org.aincraft.api.container.IEquipment;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.container.equipment.EquipmentFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GemItemFactory {

  static boolean hasContainer(NamespacedKey key, ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return false;
    }
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.has(key);
  }

  static <C extends org.aincraft.container.rework.IEffectContainer<V>, V extends org.aincraft.container.rework.IEffectContainerView>
  C containerFromStack(
      NamespacedKey key,
      ItemStack stack,
      Class<? extends C> clazz
  ) {
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    String json = pdc.get(key, PersistentDataType.STRING);
    return Taric.getGson().fromJson(json, clazz);
  }


  @Nullable
  static <C extends org.aincraft.container.rework.IEffectContainer<V>, V extends org.aincraft.container.rework.IEffectContainerView>
  C containerFromIfExists(
      ItemStack stack,
      NamespacedKey containerKey,
      Class<? extends C> containerClazz
  ) {
    Material material = stack.getType();
    if (material.isAir() || !hasContainer(containerKey, stack)) {
      return null;
    }
    return containerFromStack(containerKey, stack, containerClazz);
  }

  @Nullable
  static <H extends org.aincraft.container.rework.IEffectContainerHolder<?, V>,
      C extends org.aincraft.container.rework.IEffectContainer<V>,
      V extends org.aincraft.container.rework.IEffectContainerView>
  H holderFromIfExists(
      ItemStack stack,
      NamespacedKey containerKey,
      Class<? extends C> containerClazz,
      Function<? super C, ? extends H> holderFunction
  ) {
    C container = containerFromIfExists(stack, containerKey, containerClazz);
    return container == null ? null : holderFunction.apply(container);
  }

  @NotNull
  static <H extends IEffectContainerHolder<?, V>,
      C extends IEffectContainer<V>,
      V extends IEffectContainerView>
  H holderFrom(
      ItemStack stack,
      NamespacedKey containerKey,
      Callable<? extends H> loader,
      Class<? extends C> implementationClazz,
      Function<? super C, ? extends H> holderFunction
  ) throws IllegalArgumentException {
    Material material = stack.getType();
    if (material.isAir()) {
      throw new IllegalArgumentException("stack cannot be air");
    }

    if (!hasContainer(containerKey, stack)) {
      try {
        return loader.call();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    C container = containerFromIfExists(stack, containerKey, implementationClazz);
    if (container == null) {
      throw new IllegalArgumentException("container not found, this is a bug");
    }

    return holderFunction.apply(container);
  }

  @NotNull
  static IGemInventory inventoryFromEntity(LivingEntity livingEntity) {
    Map<EquipmentSlot, IGemItem> inventory = new HashMap<>();
    IEquipment equipment = EquipmentFactory.equipmentFromEntity(livingEntity);
    for (EquipmentSlot slot : EquipmentSlot.values()) {
      try {
        ItemStack stack = equipment.getItem(slot);
        IGemItem gemItem = GemItem.fromIfExists(stack);
        if (gemItem == null) {
          continue;
        }
        inventory.put(slot, gemItem);
      } catch (IllegalArgumentException ignored) {

      }
    }
    return new GemInventory(equipment, inventory);
  }
}
