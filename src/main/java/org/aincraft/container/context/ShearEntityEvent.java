package org.aincraft.container.context;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

interface ShearEntityEvent {

  interface IPlayerShearEntityEvent extends ShearEntityEvent {

    Player getPlayer();
  }

  ItemStack getTool();

  Entity getSheared();

  List<ItemStack> getDrops();

  void setDrops(List<ItemStack> drops);
}
