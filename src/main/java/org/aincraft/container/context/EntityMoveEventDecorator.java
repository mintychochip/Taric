package org.aincraft.container.context;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

final class EntityMoveEventDecorator implements IMoveEvent {

  private final EntityMoveEvent event;

  EntityMoveEventDecorator(EntityMoveEvent event) {
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

  Entity getEntity() {
    return event.getEntity();
  }
}
