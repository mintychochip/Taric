package org.aincraft.container.gem;

import org.aincraft.container.gem.exceptions.CapacityException;
import org.aincraft.container.gem.IGemItem.IView;
import org.aincraft.effects.IGemEffect;

public interface IGemItem extends IEffectContainerHolder<IView> {

  interface IView extends IEffectContainerView {

    int getMaxSockets();

    int getSocketsUsed();
  }

  interface IContainer extends IEffectContainer<IView> {

    default int getMaxSockets() {
      return getView().getMaxSockets();
    }

    default int getSocketsUsed() {
      return getView().getSocketsUsed();
    }

    @Override
    void addEffect(IGemEffect effect, int rank, boolean force) throws CapacityException;

    @Override
    void addEffect(IGemEffect effect, int rank) throws CapacityException;

    @Override
    void addEffect(String effect, int rank, boolean force) throws CapacityException;
  }
}
