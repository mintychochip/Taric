package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IOnBlockBreak.IBlockBreakContext;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockBreakContext extends AbstractTriggerContext<BlockBreakEvent> implements
    IBlockBreakContext {

  private boolean initial = false;

  private BlockFace blockFace = null;

  public void setInitial(boolean initial) {
    this.initial = initial;
  }

  public void setBlockFace(BlockFace blockFace) {
    this.blockFace = blockFace;
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public boolean isInitial() {
    return initial;
  }

  @Override
  public ItemStack getTool() {
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();
    return inventory.getItemInMainHand();
  }

  @Override
  public BlockFace getBlockFace() {
    return blockFace;
  }

  @Override
  public Block getBlock() {
    return event.getBlock();
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
