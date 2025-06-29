package org.aincraft.api.container.gem;

import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;

public interface IGem<C extends IGemContainer<V>, V extends IGemContainerView> extends
    IContainerHolder<C, V> {

  interface IGemContainerView extends IEffectContainerView {

    IGemEffect getEffect();

    int getRank();
  }

  interface IGemContainer<V extends IGemContainerView> extends IEffectContainer<V> {

    IGemEffect getEffect();

    int getRank();
  }
}
