package org.aincraft.api.container.gem;

import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEffectContainer<V extends IEffectContainerView> extends IItemContainer<V> {

  boolean canApplyEffect(IGemEffect effect, int rank);

  void applyEffect(IGemEffect effect, int rank)
      throws IllegalArgumentException, NullPointerException;

  void applyEffect(@NotNull IGemEffect effect, int rank, boolean force)
      throws IllegalArgumentException, NullPointerException;

  void removeEffect(@NotNull IGemEffect effect)
      throws IllegalArgumentException, NullPointerException;

  boolean hasEffect(@Nullable IGemEffect effect);

  int getRank(IGemEffect effect);

  void clear();
}
