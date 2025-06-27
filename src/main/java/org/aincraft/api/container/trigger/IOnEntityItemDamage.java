package org.aincraft.api.container.trigger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface IOnEntityItemDamage {

  interface IEntityItemDamageContext {

    double getDamage();

    void setDamage(int damage);

    ItemStack getItem();

    Entity getEntity();

    EntityType getEntityType();
  }

  void onEntityItemDamage(IEntityItemDamageContext context);
}
