package org.aincraft.api.context;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface IEntityMoveContext extends IMoveContext {

  Location getFrom();

  void setFrom(Location from);

  Location getTo();

  void setTo(Location to);

  Entity getEntity();
}
