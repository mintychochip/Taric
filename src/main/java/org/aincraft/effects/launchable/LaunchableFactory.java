package org.aincraft.effects.launchable;

import org.aincraft.api.effects.triggers.IOnShootBow.IArrowLaunchable;
import org.aincraft.api.effects.triggers.IOnShootBow.IFireworkLaunchable;
import org.aincraft.api.effects.triggers.IOnShootBow.ILaunchable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class LaunchableFactory {

  @NotNull
  public static ILaunchable create(Projectile projectile) {
    if (projectile instanceof Arrow arrow) {
      return new ArrowLaunchable(arrow);
    }
    if (projectile instanceof Firework firework) {
      return new FireworkLaunchable(firework);
    }
    return new Launchable(projectile.getClass(), projectile.getVelocity());
  }

  @NotNull
  public static ILaunchable create(@NotNull Class<? extends Projectile> clazz,
      @NotNull Vector velocity) {
    if (Arrow.class.isAssignableFrom(clazz)) {
      return new ArrowLaunchable(velocity);
    }
    if (Firework.class.isAssignableFrom(clazz)) {
      return new FireworkLaunchable(velocity);
    }
    return new Launchable(clazz, velocity);
  }

  @NotNull
  public static IArrowLaunchable createArrow(Arrow arrow) {
    return new ArrowLaunchable(arrow);
  }

  @NotNull
  public static IArrowLaunchable createArrow(Vector velocity, boolean critical, double damage) {
    return new ArrowLaunchable(velocity, critical, damage);
  }

  @NotNull
  public static IFireworkLaunchable createFirework(Vector velocity, boolean shotAtAngle,
      int ticksToDetonate, int ticksFlown, FireworkMeta meta) {
    return new FireworkLaunchable(velocity, shotAtAngle, ticksToDetonate, ticksFlown, meta);
  }

  @NotNull
  public static IFireworkLaunchable createFirework(Vector velocity, boolean shotAtAngle, int ticksToDetonate, FireworkMeta meta) {
    return createFirework(velocity,shotAtAngle,ticksToDetonate,0,meta);
  }
}
