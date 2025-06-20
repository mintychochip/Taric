package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.trigger.IShearEntityReceiver.IPlayerShearReceiver;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnPlayerShear {
  void onPlayerShear(IPlayerShearReceiver receiver);
}
