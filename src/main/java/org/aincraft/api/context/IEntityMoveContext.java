package org.aincraft.api.context;

import org.bukkit.Location;

public interface IEntityMoveContext {

  enum ChangeType {
    ORIENTATION,
    POSITION,
    BLOCK,
    EXPLICITLY_BLOCK,
    EXPLICITLY_POSITION
  }

  Location getFrom();

  void setFrom(Location from);

  Location getTo();

  void setTo(Location to);

  boolean hasChanged(ChangeType type);
}
