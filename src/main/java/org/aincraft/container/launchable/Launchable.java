package org.aincraft.container.launchable;

import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

class Launchable extends
    EntityProxy<Projectile> implements
    ILaunchable {

  protected Vector velocity;

  Launchable(Class<? extends Projectile> entityClazz, Vector velocity,
      Location location) {
    super(entityClazz, velocity, location);
  }


  @Override
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(entityClazz, velocity);
  }

  @Override
  public Vector getVelocity() {
    return velocity;
  }

  @Override
  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  @Override
  public ILaunchable clone() {
    try {
      return (ILaunchable) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
