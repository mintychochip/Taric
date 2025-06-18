package org.aincraft.api.container.gem;

import org.aincraft.api.container.gem.IGem.IContainer;
import org.aincraft.api.container.gem.IGem.IView;

public interface IGem extends IEffectContainerHolder<IView, IContainer> {

  interface IView extends IEffectContainerView {

  }

  interface IContainer extends IEffectContainer<IContainer, IView> {

  }
}
