package org.aincraft.effects;

import org.aincraft.effects.triggers.IOnShootBow.ILaunchable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

abstract class AbstractLaunchable implements ILaunchable {

  protected final Class<? extends Projectile> projectileClazz;
  protected Vector velocity;

  AbstractLaunchable(Class<? extends Projectile> projectileClazz, Vector velocity) {
    this.projectileClazz = projectileClazz;
    this.velocity = velocity;
  }

  @Override
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(projectileClazz, velocity);
  }

  @Override
  public Vector getVelocity() {
    return velocity;
  }

  @Override
  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  public Class<? extends Projectile> getProjectileClazz() {
    return projectileClazz;
  }

  @Override
  public ILaunchable clone() {
    try {
      return (AbstractLaunchable) super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }
}
