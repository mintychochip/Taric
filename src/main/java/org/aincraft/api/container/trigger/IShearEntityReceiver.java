package org.aincraft.api.container.trigger;

import org.aincraft.api.container.receiver.IReceiveDrops;
import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IShearEntityReceiver extends ITriggerReceiver, IReceiveDrops {

  @NotNull
  ItemStack getTool();

  @NotNull
  Entity getSheared();

  interface IPlayerShearReceiver extends IShearEntityReceiver {

    @NotNull
    Player getPlayer();
  }
}
