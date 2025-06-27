package org.aincraft.container;

import org.aincraft.api.container.EffectInstanceMeta;
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
  public void applyEffect(IGemEffect effect, EffectInstanceMeta meta)
      throws IllegalArgumentException, NullPointerException {
    applyEffect(effect, meta, false);
  }

  @Override
  public void applyEffect(IGemEffect effect, int rank)
      throws IllegalArgumentException, NullPointerException {
    applyEffect(effect, new EffectInstanceMeta(rank));
  }

  @Override
  public int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

}
