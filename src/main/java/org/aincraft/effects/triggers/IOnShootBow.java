package org.aincraft.effects.triggers;

import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public interface IOnShootBow {

  void onShootBow(int rank, LivingEntity launcher, List<ILaunchable> instances);

  interface ILaunchable {

    void launch(LivingEntity shooter);

    Vector getVelocity();

    void setVelocity(Vector velocity);

    ILaunchable clone();
  }

  interface IArrowLaunchable extends ILaunchable {

    void setCritical(boolean critical);

    void setDamage(double damage);

    boolean isCritical();

    double getDamage();
  }

  interface IFireworkLaunchable extends ILaunchable {

    void setTicksFlown(int ticksFlown);

    void setMeta(FireworkMeta meta);

    void setTicksToDetonate(int ticksToDetonate);

    int getTicksFlown();

    FireworkMeta getMeta();

    int getTicksToDetonate();

    void setShotAtAngle(boolean shotAtAngle);

    boolean isShotAtAngle();
  }
}
