package org.aincraft.container.trigger;

import java.util.List;
import org.aincraft.container.trigger.ShearEntityEvent.IPlayerShearEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

final class PlayerShearEntityEventDecorator implements
    IPlayerShearEntityEvent {

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
    return event.getEntity();
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
  public Player getPlayer() {
    return event.getPlayer();
  }
}
