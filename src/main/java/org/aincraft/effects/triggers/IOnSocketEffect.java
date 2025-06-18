package org.aincraft.effects.triggers;

import org.bukkit.inventory.ItemStack;

public interface IOnSocketEffect {
  void onSocket(int rank, ItemStack stack);
}
