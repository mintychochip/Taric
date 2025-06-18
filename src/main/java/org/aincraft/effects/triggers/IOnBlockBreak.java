package org.aincraft.effects.triggers;

import org.aincraft.util.Mutable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockBreak {
  void onBlockBreak(int rank, Player player, ItemStack tool, BlockFace hitFace, Block block, Mutable<Integer> experience);
}
