package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.Equipment;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemFactory;
import org.aincraft.container.EquipmentFactory;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IUnidentifiedGemFactory factory;

  private final IRegistry<IGemEffect> effectRegistry;
  private final IGemItemFactory itemFactory;
  private final ISocketGemFactory gemFactory;
  private final IRegistry<IIdentificationTable> tableRegistry;
  private final IGemIdentifier identifier;

  @Inject
  public GemCommand(
      IUnidentifiedGemFactory factory,
      IRegistry<IGemEffect> effectRegistry,
      IGemItemFactory itemFactory, ISocketGemFactory gemFactory,
      IRegistry<IIdentificationTable> tableRegistry,
      IGemIdentifier identifier) {
    this.factory = factory;
    this.effectRegistry = effectRegistry;
    this.itemFactory = itemFactory;
    this.gemFactory = gemFactory;
    this.tableRegistry = tableRegistry;
    this.identifier = identifier;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      effectRegistry.forEach(effect -> {
        ISocketGem gem = gemFactory.create(Material.EMERALD, effect);
        player.getInventory().addItem(gem.getStack());
      });
      Equipment equipment = EquipmentFactory.create(player);

// Debugging checks
      Bukkit.broadcastMessage("=== Equipment Debug Start ===");
      Bukkit.broadcastMessage("Main hand: " + String.valueOf(equipment.getItemInMainHand()));
      Bukkit.broadcastMessage("Off hand: " + String.valueOf(equipment.getItemInOffHand()));
      Bukkit.broadcastMessage("Helmet: " + String.valueOf(equipment.getHelmet()));
      Bukkit.broadcastMessage("Chestplate: " + String.valueOf(equipment.getChestplate()));
      Bukkit.broadcastMessage("Leggings: " + String.valueOf(equipment.getLeggings()));
      Bukkit.broadcastMessage("Boots: " + String.valueOf(equipment.getBoots()));

// Check slot-based access too
      for (EquipmentSlot slot : EquipmentSlot.values()) {
        try {
          Bukkit.broadcastMessage(slot.name() + ": " + String.valueOf(equipment.getItem(slot)));
        } catch (IllegalArgumentException ex) {
          Bukkit.broadcastMessage(
              slot.name() + ": threw IllegalArgumentException -> " + ex.getMessage());
        }
      }

      Bukkit.broadcastMessage("=== Equipment Debug End ===");
    }
    return false;
  }

  public void spawnLootableChest(Location location, NamespacedKey lootTableKey, long seed) {
    // 1. Place the chest block
    location.getBlock().setType(Material.CHEST);

    // 2. Get the chest's state
    BlockState state = location.getBlock().getState();
    if (!(state instanceof Chest chest)) {
      return;
    }

    // 3. Set the loot table
    LootTable lootTable = Bukkit.getLootTable(lootTableKey);
    if (lootTable == null) {
      Bukkit.getLogger().warning("Loot table not found: " + lootTableKey);
      return;
    }

    chest.setLootTable(lootTable);   // Attach loot table
    chest.setSeed(seed);             // Optional: Set seed for deterministic generation
    chest.update();                  // Push the changes to the world
  }

}
