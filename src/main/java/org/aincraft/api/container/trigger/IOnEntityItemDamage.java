package org.aincraft.api.container.trigger;

import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnEntityItemDamage {
  void onEntityItemDamage(IEntityDamageItemContext context);

  interface IEntityDamageItemContext extends ITriggerContext {
    double getDamage();
    void setDamage(int damage);
    ItemStack getItem();
    Entity getEntity();
    EntityType getEntityType();
  }
}
