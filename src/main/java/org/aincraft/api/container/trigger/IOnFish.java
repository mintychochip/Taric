package org.aincraft.api.container.trigger;

import org.aincraft.api.container.IEntityProxy;
import org.aincraft.api.container.Mutable;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public interface IOnFish {

  void onFish(int rank, Player player, FishHook hook, EquipmentSlot hand,
      Mutable<Integer> experience, Mutable<IEntityProxy> entityProxy);
}
