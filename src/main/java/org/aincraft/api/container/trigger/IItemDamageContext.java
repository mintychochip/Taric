package org.aincraft.api.container.trigger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IItemDamageContext {

  interface IPlayerItemDamageContext extends IItemDamageContext {

    Player getPlayer();

    double getOriginalDamage();
  }

  interface IEntityItemDamageContext extends IItemDamageContext {

    Entity getEntity();
  }

  ItemStack getItem();

  int getDamage();

  void setDamage(int damage);
}
