package org.aincraft.api.container.gem;

import net.kyori.adventure.key.Key;
import org.aincraft.effects.IGemEffect;

public interface IEffectContainer<C extends IEffectContainer<C, V>, V extends IEffectContainerView> extends
    IEffectContainerView,
    Cloneable {

  void addEffect(IGemEffect effect, int rank);

  void addEffect(IGemEffect effect, int rank, boolean force);

  void addEffect(Key key, int rank, boolean force);

  void removeEffect(IGemEffect effect);

  void clear();

  void move(IGemEffect effect, IEffectContainer<? extends IEffectContainer<?, ?>, ? extends IEffectContainerView> other);

  void copy(IEffectContainer<? extends C, ? extends V> other, boolean clear);

  V getView();

  C clone();
}
