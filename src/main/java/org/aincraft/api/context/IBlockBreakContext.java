package org.aincraft.api.context;

import org.aincraft.api.container.context.IExperienceContext;
import org.aincraft.events.IFakeContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IBlockBreakContext extends IExperienceContext, IFakeContext {

  Player getPlayer();

  ItemStack getTool();

  Block getBlock();
}
