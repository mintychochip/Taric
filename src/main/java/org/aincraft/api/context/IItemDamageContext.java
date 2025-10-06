package org.aincraft.api.context;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IItemDamageContext {

  interface PlayerItemDamageContext extends IItemDamageContext {

    Player getPlayer();

    double getOriginalDamage();
  }

  interface EntityItemDamageContext extends IItemDamageContext {

    Entity getEntity();
  }

  ItemStack getItem();

  int getDamage();

  void setDamage(int damage);
}
