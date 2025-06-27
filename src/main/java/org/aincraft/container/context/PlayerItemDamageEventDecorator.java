package org.aincraft.container.context;

import org.aincraft.container.context.ItemDamageEvent.IPlayerItemDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

final class PlayerItemDamageEventDecorator implements IPlayerItemDamageEvent {

  private final PlayerItemDamageEvent event;

  public PlayerItemDamageEventDecorator(PlayerItemDamageEvent event) {
    this.event = event;
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public double getOriginalDamage() {
    return event.getOriginalDamage();
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
