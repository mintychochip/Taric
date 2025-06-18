package org.aincraft.listeners;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import org.aincraft.api.container.gem.IGemInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.jetbrains.annotations.NotNull;

public class GemCacheListener implements Listener {

  private final LoadingCache<LivingEntity, IGemInventory> inventoryCache;

  @Inject
  public GemCacheListener(LoadingCache<LivingEntity, IGemInventory> inventoryCache) {
    this.inventoryCache = inventoryCache;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerChangeSlots(PlayerItemHeldEvent event) {
    invalidatePlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onSwapHand(PlayerSwapHandItemsEvent event) {
    invalidatePlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onInventoryClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player player) {
      invalidatePlayer(player);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onInventoryDrag(InventoryDragEvent event) {
    if (event.getWhoClicked() instanceof Player player) {
      invalidatePlayer(player);
    }
  }

  @EventHandler
  private void onPlayerJoin(PlayerJoinEvent event) {
    invalidatePlayer(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onItemBreak(PlayerItemBreakEvent event) {
    invalidatePlayer(event.getPlayer());
  }

  private void invalidatePlayer(@NotNull Entity entity) {
    inventoryCache.invalidate(entity);
  }
}
