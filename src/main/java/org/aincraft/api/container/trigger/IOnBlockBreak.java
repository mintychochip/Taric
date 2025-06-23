package org.aincraft.api.container.trigger;

import org.aincraft.api.container.context.IExperienceContext;
import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnBlockBreak {

  void onBlockBreak(IBlockBreakContext context);

  interface IBlockBreakContext extends ITriggerContext, IExperienceContext {

    Player getPlayer();

    boolean isInitial();

    ItemStack getTool();

    BlockFace getBlockFace();

    Block getBlock();
  }
}
