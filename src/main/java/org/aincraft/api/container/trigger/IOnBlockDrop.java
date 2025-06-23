package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.receiver.IDropContext;
import org.aincraft.api.container.receiver.ITriggerContext;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockDrop {

  void onBlockDrop(IBlockDropContext context);

  interface IBlockDropContext extends ITriggerContext, IDropContext<List<ItemStack>> {

    Player getPlayer();

    Block getBlock();

    BlockState getBlockState();
  }
}
