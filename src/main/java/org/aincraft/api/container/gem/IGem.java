package org.aincraft.api.container.gem;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGem<C extends IGemContainer<V>, V extends IGemContainerView> extends
    IContainerHolder<C, V> {

  interface IGemContainerView extends IEffectContainerView {

    @NotNull
    ISocketColor getColor();

    @Nullable
    IGemEffect getEffect();

    int getRank();
  }

  interface IGemContainer<V extends IGemContainerView> extends IEffectContainer<V> {

    @NotNull
    ISocketColor getColor();

    IGemEffect getEffect();

    int getRank();
  }
}
