package org.aincraft.effects.triggers;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnPlayerShear {
  void onPlayerShear(int rank, Player player, Entity sheared, ItemStack tool, List<ItemStack> drops);
}
