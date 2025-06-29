package org.aincraft.listeners;

import com.google.inject.Inject;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemFactory;
import org.aincraft.container.distribution.IChestDistributionConfiguration;
import org.aincraft.container.distribution.IMiningDistributionConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

public final class DistributionListener implements Listener {

  private final IUnidentifiedGemFactory unidentifiedGemFactory;
  private final IChestDistributionConfiguration chestConfiguration;
  private final IMiningDistributionConfiguration miningConfiguration;
  private final RandomGenerator randomGenerator;

  @Inject
  public DistributionListener(IUnidentifiedGemFactory unidentifiedGemFactory,
      IChestDistributionConfiguration chestConfiguration,
      IMiningDistributionConfiguration miningConfiguration, RandomGenerator randomGenerator) {
    this.unidentifiedGemFactory = unidentifiedGemFactory;
    this.chestConfiguration = chestConfiguration;
    this.miningConfiguration = miningConfiguration;
    this.randomGenerator = randomGenerator;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onLootChest(final LootGenerateEvent event) {
    if (!chestConfiguration.isEnabled()) {
      return;
    }
    InventoryHolder inventoryHolder = event.getInventoryHolder();
    if (inventoryHolder == null) {
      return;
    }
    Inventory inventory = inventoryHolder.getInventory();
    InventoryType type = inventory.getType();
    if (type != InventoryType.CHEST) {
      return;
    }
    LootTable lootTable = event.getLootTable();
    NamespacedKey key = lootTable.getKey();
    if (!chestConfiguration.getLootTableWhitelist().contains(key)) {
      return;
    }
    double chance = chestConfiguration.getBaseChance();
    Entity entity = event.getEntity();
    if (chestConfiguration.isLuckApplicable() && entity instanceof Player player) {
      if (!player.hasPermission(chestConfiguration.getPermission())) {
        return;
      }
      AttributeInstance attribute = player.getAttribute(Attribute.LUCK);
      if (attribute != null) {
        chance *= 1 + attribute.getValue() * 0.1;
      }
    }
    if (randomGenerator.nextDouble() > chance) {
      return;
    }
    int amount = randomGenerator.nextInt(chestConfiguration.getMaxAmount());
    List<ItemStack> loot = event.getLoot();
    for (int i = 0; i < amount; ++i) {
      IUnidentifiedGem gem = unidentifiedGemFactory.create(ItemStack.of(Material.EMERALD));
      loot.add(gem.getStack());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onMineApplicableMaterial(final BlockDropItemEvent event) {
    if (!miningConfiguration.isEnabled()) {
      return;
    }
    Player player = event.getPlayer();
    if (!player.hasPermission(miningConfiguration.getPermission())) {
      return;
    }
    BlockState blockState = event.getBlockState();
    Material material = blockState.getType();
    if (!miningConfiguration.getMaterialBaseChanceMap().containsKey(material)) {
      return;
    }
    Double chance = miningConfiguration.getMaterialBaseChanceMap().get(material);
    if (randomGenerator.nextDouble() > chance) {
      return;
    }
    int amount = randomGenerator.nextInt(chestConfiguration.getMaxAmount());
    for (int i = 0; i < amount; ++i) {
      IUnidentifiedGem gem = unidentifiedGemFactory.create(ItemStack.of(Material.EMERALD));
      Location location = blockState.getLocation();
      Location center = location.clone().add(0.5, 0.5, 0.5);
      World world = location.getWorld();
      Item item = world.createEntity(center, Item.class);
      item.setItemStack(gem.getStack());
      event.getItems().add(item);
    }
  }
}
