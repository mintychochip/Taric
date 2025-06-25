package org.aincraft.api.container.trigger;

import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IItemDamageContext extends ITriggerContext {

  ItemStack getItem();

  void setDamage(int damage);

  int getDamage();

  interface IPlayerItemDamageContext extends IItemDamageContext {

    Player getPlayer();
  }
}
