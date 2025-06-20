package org.aincraft.api.container;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface IEntityProxy {

  void add();

  void setVelocity(Vector velocity);

  Vector getVelocity();

  Location getLocation();

  void setLocation(Location location);
}
