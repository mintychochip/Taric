package org.aincraft.api.context;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface IEntityItemDamageContext {

  double getDamage();

  void setDamage(int damage);

  ItemStack getItem();

  Entity getEntity();

  EntityType getEntityType();
}
