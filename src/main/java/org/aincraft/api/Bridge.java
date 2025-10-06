package org.aincraft.api;

import org.aincraft.registry.RegistryAccess;
import org.bukkit.plugin.Plugin;

public interface Bridge {

  static Bridge bridge() throws IllegalStateException {
    return BridgeAccessor.bridgeAccess();
  }

  Plugin plugin();

  RegistryAccess registryAccess();
}
