package org.aincraft.container.rework;

import java.util.UUID;
import org.aincraft.effects.IGemEffect;

public interface IEffectContainer<V extends IEffectContainerView> {

  boolean setEffect(IGemEffect effect, int rank);

  boolean setEffect(IGemEffect effect, int rank, boolean force);

  void removeEffect(IGemEffect effect);

  int getRank(IGemEffect effect);

  UUID getUuid();

  void clear();

  V getView();
}
