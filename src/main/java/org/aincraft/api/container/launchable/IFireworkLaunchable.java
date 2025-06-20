package org.aincraft.api.container.launchable;

import org.bukkit.inventory.meta.FireworkMeta;

public interface IFireworkLaunchable extends ILaunchable {

  void setTicksFlown(int ticksFlown);

  void setMeta(FireworkMeta meta);

  void setTicksToDetonate(int ticksToDetonate);

  int getTicksFlown();

  FireworkMeta getMeta();

  int getTicksToDetonate();

  void setShotAtAngle(boolean shotAtAngle);

  boolean isShotAtAngle();
}
