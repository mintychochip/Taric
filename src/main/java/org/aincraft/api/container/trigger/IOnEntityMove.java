package org.aincraft.api.container.trigger;

import org.bukkit.Location;

public interface IOnEntityMove {

  interface IEntityMoveContext {

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

  void onEntityMove(IEntityMoveContext context);
}
