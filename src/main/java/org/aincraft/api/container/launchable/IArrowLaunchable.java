package org.aincraft.api.container.launchable;

public interface IArrowLaunchable extends ILaunchable {

  void setCritical(boolean critical);

  void setDamage(double damage);

  boolean isCritical();

  double getDamage();
}
