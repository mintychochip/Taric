package org.aincraft.container.context;

import org.bukkit.Location;

interface IMoveEvent {

  Location getFrom();

  void setFrom(Location from);

  Location getTo();

  void setTo(Location to);

  boolean hasChangedPosition();

  boolean hasExplicitlyChangedPosition();

  boolean hasChangedBlock();

  boolean hasExplicitlyChangedBlock();

  boolean hasChangedOrientation();
}
