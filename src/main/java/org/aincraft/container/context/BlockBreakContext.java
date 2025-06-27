package org.aincraft.container.context;

import org.aincraft.api.context.IBlockBreakContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

final class BlockBreakContext extends AbstractContext<BlockBreakEvent> implements
    IBlockBreakContext {

  private final boolean fake;

  BlockBreakContext(BlockBreakEvent event, boolean fake) {
    super(event);
    this.fake = fake;
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public ItemStack getTool() {
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();
    return inventory.getItemInMainHand();
  }

  @Override
  public Block getBlock() {
    return event.getBlock();
  }

  @Override
  public boolean isFake() {
    return fake;
  }

  @Override
  public void setExperience(int exp) {
    event.setExpToDrop(exp);
  }

  @Override
  public int getExperience() {
    return event.getExpToDrop();
  }
}
