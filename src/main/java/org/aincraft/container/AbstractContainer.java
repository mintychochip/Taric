package org.aincraft.container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import org.aincraft.api.container.gem.IItemContainer;
import org.aincraft.api.container.gem.IItemContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

abstract class AbstractContainer<V extends IItemContainerView> implements IItemContainer<V> {

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
  public V getView() {
    if (view == null) {
      view = buildView();
    }
    return view;
  }
}
