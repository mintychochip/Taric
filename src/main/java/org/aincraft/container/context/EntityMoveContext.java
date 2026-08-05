package org.aincraft.container.context;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.aincraft.api.context.IEntityMoveContext;
import org.aincraft.api.context.IMoveContext.ChangeType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Move context backed directly by the Paper entity-move event (no pass-through decorator layer).
 */
final class EntityMoveContext extends AbstractContext<EntityMoveEvent> implements
    IEntityMoveContext {

  EntityMoveContext(EntityMoveEvent event) {
    super(event);
  }

  @Override
  public Entity getEntity() {
    return event.getEntity();
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
}
