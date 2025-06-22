package org.aincraft.api.container.gem;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGem.IContainer;
import org.aincraft.api.container.gem.IGem.IView;

public interface IGem extends IEffectContainerHolder<IView, IContainer> {

  ISocketColor getSocketColor();

  interface IView extends IEffectContainerView {
    ISocketColor getSocketColor();
  }

  interface IContainer extends IEffectContainer<IContainer, IView> {
    ISocketColor getSocketColor();
  }
}
