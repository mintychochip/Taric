package org.aincraft.container.context;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

final class PlayerMoveEventDecorator implements IMoveEvent {

  private final PlayerMoveEvent event;

  PlayerMoveEventDecorator(PlayerMoveEvent event) {
    this.event = event;
  }

  @Override
  public Location getFrom() {
    return event.getFrom();
  }

  @Override
  public void setFrom(Location from) {
    event.setFrom(from);
  }

  @Override
  public Location getTo() {
    return event.getTo();
  }

  @Override
  public void setTo(Location to) {
    event.setTo(to);
  }

  @Override
  public boolean hasChangedPosition() {
    return event.hasChangedPosition();
  }

  @Override
  public boolean hasExplicitlyChangedPosition() {
    return event.hasExplicitlyChangedPosition();
  }

  @Override
  public boolean hasChangedBlock() {
    return event.hasChangedBlock();
  }

  @Override
  public boolean hasExplicitlyChangedBlock() {
    return event.hasExplicitlyChangedBlock();
  }

  @Override
  public boolean hasChangedOrientation() {
    return event.hasChangedOrientation();
  }

  public Player getPlayer() {
    return event.getPlayer();
  }
}
