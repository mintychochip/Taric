package org.aincraft.api.container.gem;

import java.util.UUID;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEffectContainer<V extends IEffectContainerView> {

  boolean canApplyEffect(IGemEffect effect, EffectInstanceMeta meta);

  void applyEffect(IGemEffect effect, EffectInstanceMeta meta)
      throws IllegalArgumentException, NullPointerException;

  void applyEffect(IGemEffect effect, int rank)
      throws IllegalArgumentException, NullPointerException;

  void applyEffect(@NotNull IGemEffect effect, EffectInstanceMeta meta, boolean force)
      throws IllegalArgumentException, NullPointerException;

  void removeEffect(@NotNull IGemEffect effect)
      throws IllegalArgumentException, NullPointerException;

  boolean hasEffect(@Nullable IGemEffect effect);

  int getRank(IGemEffect effect);

  void clear();

  UUID getUuid();

  V getView();
}
