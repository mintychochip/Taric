package org.aincraft.container.trigger;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

interface ShearEntityEvent<E extends Event> {

  ItemStack getTool();

  Entity getSheared();

  List<ItemStack> getDrops();

  void setDrops(List<ItemStack> drops);

  E getHandle();

}
