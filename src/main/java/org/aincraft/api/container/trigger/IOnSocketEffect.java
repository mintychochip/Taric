package org.aincraft.api.container.trigger;

import org.bukkit.inventory.ItemStack;

public interface IOnSocketEffect {
  void onSocket(int rank, ItemStack stack);
}
