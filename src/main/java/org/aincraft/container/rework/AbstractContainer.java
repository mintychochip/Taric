package org.aincraft.container.rework;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;

abstract class AbstractContainer<V extends IEffectContainerView> implements IEffectContainer<V> {

  private V view = null;

  @Expose
  @SerializedName("container-key")
  private final Key containerKey;

  @Expose
  @SerializedName("uuid")
  private final UUID uuid;

  AbstractContainer(NamespacedKey containerKey) {
    this.containerKey = containerKey;
    this.uuid = UUID.randomUUID();
  }

  NamespacedKey getContainerKey() {
    return (NamespacedKey) containerKey;
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  protected abstract V buildView();

  @Override
  public boolean setEffect(IGemEffect effect, int rank) {
    return setEffect(effect, rank, false);
  }

  @Override
  public int getRank(IGemEffect effect) {
    return getView().getRank(effect);
  }

  @Override
  public V getView() {
    if (view == null) {
      view = buildView();
    }
    return view;
  }
}
