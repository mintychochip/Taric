package org.aincraft.container.gem;

import com.google.gson.annotations.Expose;
import java.util.Map;
import java.util.Map.Entry;
import org.aincraft.Taric;
import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.Nullable;

abstract class AbstractEffectContainer<T extends IEffectContainerView> implements
    IEffectContainer<T> {

  @Expose(serialize = false, deserialize = false)
  private T view = null;

  protected abstract Map<String, Integer> delegate();

  protected abstract T buildView();

  @Override
  public T getView() {
    if (view == null) {
      view = buildView();
    }
    return view;
  }

  @Override
  public void addEffect(IGemEffect effect, int rank) {
    this.addEffect(effect, rank, false);
  }

  @Override
  public void addEffect(IGemEffect effect, int rank, boolean force) {
    rank = !force ? Math.min(rank, effect.getMaxLevel()) : rank;
    delegate().put(effect.getKey(), rank);
  }

  @Override
  public void addEffect(String effect, int rank, boolean force) {
    IGemEffect gemEffect = Taric.getEffects().get(effect);
    if (gemEffect == null) {
      return;
    }
    addEffect(gemEffect, rank, force);
  }

  @Override
  public int getRank(IGemEffect effect) {
    return delegate().get(effect.getKey());
  }

  @Override
  public void removeEffect(IGemEffect effect) {
    if (!delegate().containsKey(effect.getKey())) {
      return;
    }
    delegate().remove(effect.getKey());
  }

  @Override
  public void clear() {
    delegate().clear();
  }

  @Override
  public void move(IGemEffect effect, IEffectContainer<? extends T> other) {
    if (!has(effect) || other.has(effect)) {
      return;
    }
    Integer rank = delegate().get(effect.getKey());
    other.addEffect(effect, rank);
    this.removeEffect(effect);
  }

  @Override
  public void copy(IEffectContainer<? extends T> other, boolean clear) {
    if (clear) {
      other.clear();
    }
    for (Entry<String, Integer> entry : delegate().entrySet()) {
      other.addEffect(entry.getKey(), entry.getValue(), true);
    }
  }

  @Override
  public boolean has(IGemEffect effect) {
    return delegate().containsKey(effect.getKey());
  }

  @Override
  public String toString() {
    return delegate().toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public IEffectContainer<T> clone() {
    try {
      return (IEffectContainer<T>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
