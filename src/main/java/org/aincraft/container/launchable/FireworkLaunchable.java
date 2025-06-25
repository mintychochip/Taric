package org.aincraft.container.launchable;

import org.aincraft.api.container.launchable.IFireworkLaunchable;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

final class FireworkLaunchable extends Launchable implements IFireworkLaunchable {

  private boolean shotAtAngle;
  private int ticksToDetonate;
  private int ticksFlown;
  private FireworkMeta meta;

  FireworkLaunchable(
      Vector velocity, Location location, boolean shotAtAngle, int ticksToDetonate, int ticksFlown,
      FireworkMeta meta) {
    super(Firework.class, velocity, location);
    this.shotAtAngle = shotAtAngle;
    this.ticksToDetonate = ticksToDetonate;
    this.ticksFlown = ticksFlown;
    this.meta = meta;
  }

  FireworkLaunchable(Vector velocity, Location location) {
    super(Firework.class, velocity, location);
  }

  FireworkLaunchable(Firework firework) {
    this(firework.getVelocity(), firework.getLocation(), firework.isShotAtAngle(),
        firework.getTicksFlown(),
        firework.getTicksToDetonate(), firework.getFireworkMeta());
  }

  @Override
  public void setTicksFlown(int ticksFlown) {
    this.ticksFlown = ticksFlown;
  }

  @Override
  public void setMeta(FireworkMeta meta) {
    this.meta = meta;
  }

  @Override
  public void setTicksToDetonate(int ticksToDetonate) {
    this.ticksToDetonate = ticksToDetonate;
  }

  @Override
  public int getTicksFlown() {
    return ticksFlown;
  }

  @Override
  public FireworkMeta getMeta() {
    return meta;
  }

  @Override
  public int getTicksToDetonate() {
    return ticksToDetonate;
  }

  @Override
  public void setShotAtAngle(boolean shotAtAngle) {
    this.shotAtAngle = shotAtAngle;
  }

  @Override
  public boolean isShotAtAngle() {
    return shotAtAngle;
  }

  @Override
  public Projectile launch(LivingEntity shooter) {
    return shooter.launchProjectile(Firework.class, velocity, f -> {
      f.setShotAtAngle(shotAtAngle);
      f.setTicksToDetonate(ticksToDetonate);
      f.setTicksFlown(ticksFlown);
      f.setFireworkMeta(meta);
    });
  }

  @Override
  public ILaunchable clone() {
    return new FireworkLaunchable(velocity, location, shotAtAngle, ticksToDetonate, ticksFlown,
        meta);
  }
}