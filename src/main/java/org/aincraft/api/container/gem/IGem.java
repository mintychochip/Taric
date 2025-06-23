package org.aincraft.api.container.gem;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;

public interface IGem extends IItemContainerHolder<IGemContainer, IGemContainerView> {

  ISocketColor getSocketColor();

  interface IGemContainer extends IEffectContainer<IGemContainerView> {

    ISocketColor getSocketColor();

    IGemEffect getEffect();

    int getRank();

    void move(IItemContainerHolder<? extends IEffectContainer<?>,?> holder);
  }

  interface IGemContainerView extends IEffectContainerView {

    ISocketColor getSocketColor();

    IGemEffect getEffect();

    int getRank();
  }

  interface IGemFactory extends IItemHolderFactory<IGem,IGemContainer,IGemContainerView> {
    IGem create(Material material, ISocketColor socketColor);
  }
}
