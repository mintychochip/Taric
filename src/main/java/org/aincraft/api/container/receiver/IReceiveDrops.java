package org.aincraft.api.container.receiver;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface IReceiveDrops {
  void setDrops(List<ItemStack> drops);
  List<ItemStack> getDrops();
}
