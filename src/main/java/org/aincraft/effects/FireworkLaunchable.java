package org.aincraft.effects;

import org.aincraft.effects.triggers.IOnShootBow;
import org.aincraft.effects.triggers.IOnShootBow.IFireworkLaunchable;
import org.aincraft.effects.triggers.IOnShootBow.ILaunchable;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public final class FireworkLaunchable extends AbstractLaunchable implements IFireworkLaunchable {

  private boolean shotAtAngle;
  private int ticksToDetonate;
  private int ticksFlown;
  private FireworkMeta meta;

  public FireworkLaunchable(
      Vector velocity, boolean shotAtAngle, int ticksToDetonate, int ticksFlown,
      FireworkMeta meta) {
    super(Firework.class, velocity);
    this.shotAtAngle = shotAtAngle;
    this.ticksToDetonate = ticksToDetonate;
    this.ticksFlown = ticksFlown;
    this.meta = meta;
  }

  public FireworkLaunchable(
      Vector velocity, boolean shotAtAngle, int ticksToDetonate,
      FireworkMeta meta) {
    this(velocity, shotAtAngle, ticksToDetonate, 0, meta);
  }

  public FireworkLaunchable(Firework firework) {
    this(firework.getVelocity(), firework.isShotAtAngle(), firework.getTicksFlown(),
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
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(Firework.class, velocity, f -> {
      f.setShotAtAngle(shotAtAngle);
      f.setTicksToDetonate(ticksToDetonate);
      f.setTicksFlown(ticksFlown);
      f.setFireworkMeta(meta);
    });
  }

  @Override
  public ILaunchable clone() {
    return new FireworkLaunchable(velocity, shotAtAngle, ticksToDetonate, ticksFlown, meta);
  }
}