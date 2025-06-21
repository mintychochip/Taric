package org.aincraft.listeners;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.aincraft.api.container.gem.IGem;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.container.gem.Gem;
import org.aincraft.container.gem.GemItem;
import org.aincraft.container.gui.SocketGui;
import org.aincraft.effects.gems.Effects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SocketGemListener implements Listener {
}
