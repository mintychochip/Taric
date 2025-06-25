package org.aincraft.container.trigger;

import org.aincraft.container.trigger.ItemDamageEvent.IPlayerItemDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public final class PlayerItemDamageEventDecorator implements IPlayerItemDamageEvent {

  private final PlayerItemDamageEvent event;

  public PlayerItemDamageEventDecorator(PlayerItemDamageEvent event) {
    this.event = event;
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public ItemStack getItem() {
    return event.getItem();
  }

  @Override
  public int getDamage() {
    return event.getDamage();
  }

  @Override
  public void setDamage(int damage) {
    event.setDamage(damage);
  }
}
