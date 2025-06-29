package org.aincraft.container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;

abstract class AbstractContainer<V extends IEffectContainerView> implements IEffectContainer<V> {

  @Expose
  @SerializedName("container-key")
  protected final Key containerKey;
  @Expose
  @SerializedName("uuid")
  protected final UUID uuid;
  private V view = null;

  AbstractContainer(NamespacedKey containerKey) {
    this.containerKey = containerKey;
    uuid = UUID.randomUUID();
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public V getView() {
    if (view == null) {
      view = buildView();
    }
    return view;
  }

  protected abstract V buildView();

  protected NamespacedKey getContainerKey() {
    return (NamespacedKey) containerKey;
  }

  @Override
  public void applyEffect(IGemEffect effect, EffectInstanceMeta meta)
      throws IllegalArgumentException, NullPointerException {
    applyEffect(effect, meta, false);
  }

  @Override
  public void applyEffect(IGemEffect effect, int rank)
      throws IllegalArgumentException, NullPointerException {
    applyEffect(effect, new EffectInstanceMeta(rank));
  }


}
