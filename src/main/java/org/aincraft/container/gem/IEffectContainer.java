package org.aincraft.container.gem;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.aincraft.Taric;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public interface IEffectContainer<View extends IEffectContainerView> extends IEffectContainerView,
    Cloneable {

  static <Container extends IEffectContainer<View>, View extends IEffectContainerView> Container from(NamespacedKey key, ItemStack stack,
      Class<Container> clazz) {
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    String json = pdc.get(key, PersistentDataType.STRING);
    return Taric.getGson().fromJson(json, clazz);
  }

  static boolean hasContainer(NamespacedKey key, ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return false;
    }
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.has(key);
  }

  void addEffect(IGemEffect effect, int rank);

  void addEffect(IGemEffect effect, int rank, boolean force);

  void addEffect(String effect, int rank, boolean force);

  void removeEffect(IGemEffect effect);

  void clear();

  void move(IGemEffect effect, IEffectContainer<? extends View> other);

  void copy(IEffectContainer<? extends View> other, boolean clear);

  View getView();

  default int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

  default void update(ItemStack stack) {
    getView().update(stack);
  }

  default boolean has(IGemEffect effect) {
    return getView().has(effect);
  }

  default NamespacedKey getKey() {
    return getView().getKey();
  }

  IEffectContainer<View> clone();
}
