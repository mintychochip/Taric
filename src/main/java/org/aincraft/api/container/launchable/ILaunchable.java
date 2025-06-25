package org.aincraft.api.container.launchable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public interface ILaunchable {

  Projectile launch(LivingEntity shooter);

  Vector getVelocity();

  void setVelocity(Vector velocity);

  Location getLocation();

  ILaunchable clone();
}
