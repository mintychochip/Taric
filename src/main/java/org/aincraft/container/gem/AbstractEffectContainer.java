package org.aincraft.container.gem;

import com.google.gson.annotations.Expose;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractEffectContainer<C extends IEffectContainer<C, V>, V extends IEffectContainerView> implements
    IEffectContainer<C, V> {

  @Expose(deserialize = false, serialize = false)
  private V view = null;

  protected abstract Map<Key, Integer> delegate();

  protected abstract V buildView();

  @Override
  public V getView() {
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
    rank = !force ? Math.min(rank, effect.getMaxRank()) : rank;
    delegate().put(effect.getKey(), rank);
  }

  @Override
  public void addEffect(Key key, int rank, boolean force) {
    IGemEffect effect = Taric.getEffects().get(key);
    if (effect == null) {
      return;
    }
    addEffect(effect, rank, force);
  }

  @Override
  public int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

  @Override
  public void removeEffect(IGemEffect effect) {
    if (!delegate().containsKey(effect.key())) {
      return;
    }
    delegate().remove(effect.key());
  }

  @Override
  public void clear() {
    delegate().clear();
  }

  @Override
  public void move(IGemEffect effect,
      IEffectContainer<? extends IEffectContainer<?, ?>, ? extends IEffectContainerView> other) {
    if (!has(effect) || other.has(effect)) {
      return;
    }
    Integer rank = delegate().get(effect.key());
    other.addEffect(effect, rank);
    this.removeEffect(effect);
  }

  @Override
  public void copy(IEffectContainer<? extends C, ? extends V> other, boolean clear) {
    if (clear) {
      other.clear();
    }
    for (Entry<Key, Integer> entry : delegate().entrySet()) {
      other.addEffect(entry.getKey(), entry.getValue(), true);
    }
  }

  @Override
  public boolean has(IGemEffect effect) {
    return getView().has(effect);
  }

  @Override
  public String toString() {
    return delegate().toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public C clone() {
    try {
      return (C) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(ItemStack stack) {
    getView().update(stack);
  }

  @Override
  public NamespacedKey getKey() {
    return getView().getKey();
  }

  @NotNull
  @Override
  public Iterator<Entry<Key, Integer>> iterator() {
    return getView().iterator();
  }
}
