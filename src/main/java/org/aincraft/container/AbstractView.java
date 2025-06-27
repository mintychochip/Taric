package org.aincraft.container;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

abstract class AbstractView<CImpl extends AbstractContainer<V>, V extends IEffectContainerView> implements
    IEffectContainerView {

  protected final CImpl container;

  AbstractView(CImpl container) {
    this.container = container;
  }

  @Override
  public void update(ItemStack stack) {
    if (stack == null || stack.getType() == Material.AIR) {
      return;
    }

    Component title = this.toItemTitle();
    ItemLore lore = this.toItemLore();
    Key itemModel = this.toItemModel();
    stack.setData(DataComponentTypes.ITEM_NAME, title);
    stack.setData(DataComponentTypes.LORE, lore);
    if (itemModel != null) {
      stack.setData(DataComponentTypes.ITEM_MODEL, itemModel);
    }
    stack.editPersistentDataContainer(pdc -> {
      String json = Taric.getGson().toJson(container);
      pdc.set(container.getContainerKey(),
          PersistentDataType.STRING, json);
    });
  }

  protected Component toItemTitle() {
    return Component.empty();
  }

  @SuppressWarnings("UnstableApiUsage")
  protected ItemLore toItemLore() {
    return ItemLore.lore().build();
  }

  @Nullable
  protected Key toItemModel() {
    return null;
  }

  @Override
  public String toString() {
    return container.toString();
  }

  @Override
  public int getRank(IGemEffect effect) {
    return container.getRank(effect);
  }
}
