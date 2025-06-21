package org.aincraft.api.container.gem;

import net.kyori.adventure.key.Key;
import org.aincraft.api.container.gem.IGemItem.IContainer;
import org.aincraft.api.container.gem.IGemItem.IView;
import org.aincraft.api.exceptions.CapacityException;
import org.aincraft.api.exceptions.TargetTypeException;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;

public interface IGemItem extends IEffectContainerHolder<IView, IContainer> {

  interface IView extends IEffectContainerView {

    int getMaxSockets();

    int getSocketsUsed();

    Material getMaterial();
  }

  interface IContainer extends IEffectContainer<IContainer, IView> {

    int getMaxSockets();

    int getSocketsUsed();

    Material getMaterial();

    @Override
    void addEffect(IGemEffect effect, int rank, boolean force)
        throws CapacityException, TargetTypeException;

    @Override
    void addEffect(IGemEffect effect, int rank) throws CapacityException, TargetTypeException;

    @Override
    void addEffect(Key key, int rank, boolean force) throws CapacityException, TargetTypeException;
  }
}
