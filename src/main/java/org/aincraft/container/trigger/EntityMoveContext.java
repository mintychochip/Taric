package org.aincraft.container.trigger;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.aincraft.api.container.trigger.IOnEntityMove.IEntityMoveContext;
import org.bukkit.Location;

final class EntityMoveContext extends AbstractContext<EntityMoveEvent> implements
    IEntityMoveContext {

  EntityMoveContext(EntityMoveEvent event) {
    super(event);
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
  public boolean hasChanged(ChangeType type) {
    return switch (type) {
      case BLOCK -> event.hasChangedBlock();
      case POSITION -> event.hasChangedPosition();
      case ORIENTATION -> event.hasChangedOrientation();
      case EXPLICITLY_BLOCK -> event.hasExplicitlyChangedBlock();
      case EXPLICITLY_POSITION -> event.hasExplicitlyChangedPosition();
    };
  }
}
