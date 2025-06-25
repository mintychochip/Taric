package org.aincraft.container.trigger;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.aincraft.api.container.trigger.IOnEntityItemDamage.IEntityDamageItemContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageItemContext extends
    AbstractTriggerContext<EntityDamageItemEvent> implements
    IEntityDamageItemContext {

  @Override
  public double getDamage() {
    return event.getDamage();
  }

  @Override
  public void setDamage(int damage) {
    event.setDamage(damage);
  }

  @Override
  public ItemStack getItem() {
    return event.getItem();
  }

  @Override
  public Entity getEntity() {
    return event.getEntity();
  }

  @Override
  public EntityType getEntityType() {
    return event.getEntityType();
  }
}
