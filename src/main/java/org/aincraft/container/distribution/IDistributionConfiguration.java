package org.aincraft.container.distribution;

import org.bukkit.permissions.Permission;

interface IDistributionConfiguration {

  boolean isEnabled();

  Permission getPermission();
}
