package org.aincraft.api.container.gem;

import org.aincraft.effects.IGemEffect;

public interface IEffectContainerView extends IItemContainerView {
  int getRank(IGemEffect effect);
}
