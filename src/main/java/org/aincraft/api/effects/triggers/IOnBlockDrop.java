package org.aincraft.api.effects.triggers;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockDrop {
  void onBlockDrop(int rank, Player player, Block block, BlockState blockState, List<ItemStack> drops);
}
