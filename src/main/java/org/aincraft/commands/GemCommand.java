package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.Rarities;
import org.aincraft.api.container.SocketColors;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemFactory;
import org.aincraft.api.trigger.ITriggerType;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IUnidentifiedGemFactory factory;

  private final IRegistry<ITriggerType<?>> triggerRegistry;
  private final IRegistry<IGemEffect> effectRegistry;
  private final IGemItemFactory itemFactory;
  private final ISocketGemFactory gemFactory;
  private final IRegistry<IIdentificationTable> tableRegistry;
  private final IGemIdentifier identifier;

  @Inject
  public GemCommand(
      IUnidentifiedGemFactory factory, IRegistry<ITriggerType<?>> triggerRegistry,
      IRegistry<IGemEffect> effectRegistry,
      IGemItemFactory itemFactory, ISocketGemFactory gemFactory,
      IRegistry<IIdentificationTable> tableRegistry,
      IGemIdentifier identifier) {
    this.factory = factory;
    this.triggerRegistry = triggerRegistry;
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
      IUnidentifiedGem gem = factory.create(ItemStack.of(Material.EMERALD), SocketColors.YELLOW,
          Rarities.MYTHIC);
      player.getInventory().addItem(gem.getStack());
//      IGemItem item = itemFactory.create(ItemStack.of(Material.DIAMOND_BOOTS));
//      item.editContainer(container -> {
//        container.applyEffect(Effects.LAVA_WALKER, new EffectInstanceMeta(3), true);
//      });
//      player.getInventory().addItem(item.getStack());
//      NamespacedKey lootKey = new NamespacedKey("minecraft", "chests/simple_dungeon");
//      spawnLootableChest(player.getLocation(), lootKey, new Random().nextLong());
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
