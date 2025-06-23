package org.aincraft.api.container.gem;

import org.aincraft.effects.IGemEffect;

public interface IEffectContainer<V extends IEffectContainerView> extends IItemContainer<V> {

  boolean setEffect(IGemEffect effect, int rank);

  boolean setEffect(IGemEffect effect, int rank, boolean force);

  void removeEffect(IGemEffect effect);

  int getRank(IGemEffect effect);

  void clear();
}
