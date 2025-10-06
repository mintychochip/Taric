package org.aincraft;

import com.google.inject.Inject;
import org.aincraft.api.Bridge;
import org.aincraft.registry.RegistryAccess;
import org.bukkit.plugin.Plugin;

public class BridgeImpl implements Bridge {

  private final Plugin plugin;
  private final RegistryAccess registryAccess;

  @Inject
  public BridgeImpl(Plugin plugin, RegistryAccess registryAccess) {
    this.plugin = plugin;
    this.registryAccess = registryAccess;
  }

  @Override
  public Plugin plugin() {
    return plugin;
  }

  @Override
  public RegistryAccess registryAccess() {
    return registryAccess;
  }
}
