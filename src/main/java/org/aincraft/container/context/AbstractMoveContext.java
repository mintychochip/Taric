package org.aincraft.container.context;

import org.aincraft.api.context.IMoveContext;
import org.bukkit.Location;

abstract class AbstractMoveContext<E extends IMoveEvent> extends AbstractContext<E> implements
    IMoveContext {

  AbstractMoveContext(E event) {
    super(event);
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
