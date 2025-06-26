package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.context.IDropContext;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockDrop {

  interface IBlockDropContext extends IDropContext<List<ItemStack>> {

    Player getPlayer();

    Block getBlock();

    BlockState getBlockState();
  }

  void onBlockDrop(IBlockDropContext context, int rank);
}
