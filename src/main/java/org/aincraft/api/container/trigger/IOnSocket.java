package org.aincraft.api.container.trigger;

import org.bukkit.inventory.ItemStack;

public interface IOnSocket {
  void onSocket(ItemStack stack);
  void onUnSocket(ItemStack stack);
}
