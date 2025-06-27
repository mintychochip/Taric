package org.aincraft.api.trigger;

import org.bukkit.inventory.ItemStack;

public interface IOnSocket {

  void onSocket(ItemStack stack);

  void onUnSocket(ItemStack stack);
}
