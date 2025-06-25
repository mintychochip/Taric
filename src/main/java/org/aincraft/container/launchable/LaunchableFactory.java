package org.aincraft.container.launchable;

import org.aincraft.api.container.launchable.IArrowLaunchable;
import org.aincraft.api.container.launchable.IFireworkLaunchable;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    return new Launchable(projectile.getClass(), projectile.getVelocity(),
        projectile.getLocation());
  }

  @NotNull
  public static IArrowLaunchable createArrow(Arrow arrow) {
    return new ArrowLaunchable(arrow);
  }

  @NotNull
  public static IArrowLaunchable createArrow(Vector velocity, Location location, boolean critical, double damage) {
    return new ArrowLaunchable(velocity, location, critical, damage);
  }

  @NotNull
  public static IFireworkLaunchable createFirework(Vector velocity, Location location,
      boolean shotAtAngle,
      int ticksToDetonate, int ticksFlown, FireworkMeta meta) {
    return new FireworkLaunchable(velocity, location, shotAtAngle, ticksToDetonate, ticksFlown,
        meta);
  }

  @NotNull
  public static IFireworkLaunchable createFirework(Vector velocity, Location location,
      boolean shotAtAngle,
      int ticksToDetonate, FireworkMeta meta) {
    return createFirework(velocity, location, shotAtAngle, ticksToDetonate, 0, meta);
  }
}
