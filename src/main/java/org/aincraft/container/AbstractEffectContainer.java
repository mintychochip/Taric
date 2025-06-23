package org.aincraft.container;

import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;

abstract class AbstractEffectContainer<V extends IEffectContainerView> extends
    AbstractContainer<V> implements IEffectContainer<V> {

  AbstractEffectContainer(NamespacedKey containerKey) {
    super(containerKey);
  }

  @Override
  public boolean setEffect(IGemEffect effect, int rank) {
    return setEffect(effect, rank, false);
  }

  @Override
  public int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

}
