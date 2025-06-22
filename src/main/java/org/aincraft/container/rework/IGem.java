package org.aincraft.container.rework;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGem.IGemContainer;
import org.aincraft.container.rework.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;

public interface IGem extends IEffectContainerHolder<IGemContainer, IGemContainerView> {

  ISocketColor getSocketColor();

  interface IGemContainer extends IEffectContainer<IGemContainerView> {

    ISocketColor getSocketColor();

    IGemEffect getEffect();

    int getRank();

    void move(IEffectContainerHolder<?,?> holder);
  }

  interface IGemContainerView extends IEffectContainerView {

    ISocketColor getSocketColor();

    IGemEffect getEffect();

    int getRank();
  }
}
