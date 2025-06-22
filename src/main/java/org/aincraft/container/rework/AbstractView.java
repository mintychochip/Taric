package org.aincraft.container.rework;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import org.aincraft.Taric;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

abstract class AbstractView<CImpl extends AbstractContainer<V>, C extends IEffectContainer<V>, V extends IEffectContainerView> implements
    IEffectContainerView {

  protected final CImpl container;

  protected abstract Component toItemTitle();

  @SuppressWarnings("UnstableApiUsage")
  protected abstract ItemLore toItemLore();

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
    stack.setData(DataComponentTypes.ITEM_NAME, title);
    stack.setData(DataComponentTypes.LORE, lore);
    stack.editPersistentDataContainer(pdc -> pdc.set(container.getContainerKey(),
        PersistentDataType.STRING, Taric.getGson().toJson(container)));
  }
}
