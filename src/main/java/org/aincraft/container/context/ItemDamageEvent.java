package org.aincraft.container.context;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

interface ItemDamageEvent {

  interface IPlayerItemDamageEvent extends ItemDamageEvent {

    Player getPlayer();

    double getOriginalDamage();
  }

  interface IEntityDamageItemEvent extends ItemDamageEvent {

    Entity getEntity();

    EntityType getEntityType();
  }

  ItemStack getItem();

  int getDamage();

  void setDamage(int damage);
}
