package org.aincraft.api.context;

import org.bukkit.Location;

public interface IMoveContext {

  enum ChangeType {
    ORIENTATION,
    POSITION,
    BLOCK,
    EXPLICITLY_BLOCK,
    EXPLICITLY_POSITION
  }

  boolean hasChanged(ChangeType type);

  Location getFrom();

  void setFrom(Location from);

  Location getTo();

  void setTo(Location to);

}
