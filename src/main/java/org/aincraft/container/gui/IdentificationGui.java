package org.aincraft.container.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class IdentificationGui implements InventoryHolder {

  private final Inventory inventory;

  public IdentificationGui() {
    this.inventory = Bukkit.createInventory(this, InventoryType.SMITHING,
        Component.text("Identifier"));
  }

  @Override
  public @NotNull Inventory getInventory() {
    return null;
  }
}
