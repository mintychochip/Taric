package org.aincraft.container.rework;

import org.aincraft.effects.IGemEffect;

public interface IEffectContainer<V extends IEffectContainerView> {

  boolean setEffect(IGemEffect effect, int rank);

  boolean setEffect(IGemEffect effect, int rank, boolean force);

  int getRank(IGemEffect effect);

  void clear();

  V getView();
}
