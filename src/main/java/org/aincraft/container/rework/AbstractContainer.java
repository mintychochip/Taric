package org.aincraft.container.rework;

import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;

abstract class AbstractContainer<V extends IEffectContainerView> implements IEffectContainer<V> {

  private V view = null;

  private final NamespacedKey containerKey;

  AbstractContainer(NamespacedKey containerKey) {
    this.containerKey = containerKey;
  }

  NamespacedKey getContainerKey() {
    return containerKey;
  }

  protected abstract V buildView();

  @Override
  public boolean setEffect(IGemEffect effect, int rank) {
    return setEffect(effect, rank, false);
  }

  @Override
  public int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

  @Override
  public V getView() {
    if (view == null) {
      view = buildView();
    }
    return view;
  }
}
