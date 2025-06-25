package org.aincraft.api.container.trigger;

import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.Location;

public interface IOnEntityMove {

  void onEntityMove(IEntityMoveContext context);

  interface IEntityMoveContext extends ITriggerContext {

    void setFrom(Location from);

    void setTo(Location to);

    Location getFrom();

    Location getTo();

    boolean hasChanged(ChangeType type);

    enum ChangeType {
      ORIENTATION,
      POSITION,
      BLOCK,
      EXPLICITLY_BLOCK,
      EXPLICITLY_POSITION
    }
  }
}
