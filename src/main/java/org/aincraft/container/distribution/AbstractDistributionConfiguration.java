package org.aincraft.container.distribution;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

abstract class AbstractDistributionConfiguration implements IDistributionConfiguration {

  private final boolean enabled;
  private final int maxAmount;
  private final Permission permission;

  AbstractDistributionConfiguration(boolean enabled, int maxAmount, Permission permission) {
    this.enabled = enabled;
    this.permission = permission;
    this.maxAmount = maxAmount;
    Bukkit.getPluginManager().addPermission(permission);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public Permission getPermission() {
    return permission;
  }

  public int getMaxAmount() {
    return maxAmount;
  }
}
