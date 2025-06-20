package org.aincraft.api.container.trigger;

import org.aincraft.api.container.Mutable;
import org.aincraft.api.container.receiver.IReceiveExperience;
import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockBreak {
  void onBlockBreak(IBlockBreakReceiver receiver);
//  void onBlockBreak(int rank, Player player, ItemStack tool, BlockFace hitFace, Block block, Mutable<Integer> experience);

  interface IBlockBreakReceiver extends ITriggerReceiver, IReceiveExperience {
    Player getPlayer();
    boolean isInitial();
    ItemStack getTool();
    BlockFace getBlockFace();
    Block getBlock();
  }
}
