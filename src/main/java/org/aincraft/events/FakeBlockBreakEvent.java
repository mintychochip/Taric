package org.aincraft.events;

import org.aincraft.Taric;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public final class FakeBlockBreakEvent extends BlockBreakEvent {


  @SuppressWarnings("UnstableApiUsage")
  public FakeBlockBreakEvent(@NotNull Block block,
      @NotNull Player player) {
    super(block, player);
    PlayerInventory inventory = player.getInventory();
    ItemStack mainHand = inventory.getItemInMainHand();
    if (mainHand.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) {
      this.setExpToDrop(calculateBlockXp(block.getType()));
    }
  }

  private static int calculateBlockXp(Material material) {
    return switch (material) {
      case COAL_ORE, DEEPSLATE_COAL_ORE -> Taric.getRandom().nextInt(0, 3);
      case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> Taric.getRandom().nextInt(3, 8);
      case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> Taric.getRandom().nextInt(3, 8);
      case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> Taric.getRandom().nextInt(2, 7);
      case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> Taric.getRandom().nextInt(1, 6);
      case NETHER_QUARTZ_ORE -> Taric.getRandom().nextInt(2, 6);
      case NETHER_GOLD_ORE -> Taric.getRandom().nextInt(0, 2);
      case SPAWNER -> Taric.getRandom().nextInt(15, 44);
      default -> 0;
    };
  }
}
