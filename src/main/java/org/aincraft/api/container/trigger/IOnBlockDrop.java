package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.receiver.IReceiveDrops;
import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockDrop {

  void onBlockDrop(IBlockDropReceiver receiver);

  interface IBlockDropReceiver extends ITriggerReceiver, IReceiveDrops {

    Player getPlayer();

    Block getBlock();

    BlockState getBlockState();
  }
}
