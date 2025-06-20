package org.aincraft.container.trigger;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

final class PlayerShearEntityEventDecorator implements
    org.aincraft.container.trigger.PlayerShearEntityEvent {

  private final PlayerShearEntityEvent event;

  PlayerShearEntityEventDecorator(PlayerShearEntityEvent event) {
    this.event = event;
  }

  @Override
  public ItemStack getTool() {
    return event.getItem();
  }

  @Override
  public Entity getSheared() {
    return null;
  }

  @Override
  public List<ItemStack> getDrops() {
    return event.getDrops();
  }

  @Override
  public void setDrops(List<ItemStack> drops) {
    event.setDrops(drops);
  }

  @Override
  public PlayerShearEntityEvent getHandle() {
    return event;
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }
}
