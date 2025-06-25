package org.aincraft.container.trigger;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemDamageEvent {

  ItemStack getItem();

  int getDamage();

  void setDamage(int damage);

  interface IPlayerItemDamageEvent extends ItemDamageEvent {

    Player getPlayer();
  }
}
