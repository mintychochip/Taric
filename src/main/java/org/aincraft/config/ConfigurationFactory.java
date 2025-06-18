package org.aincraft.config;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ConfigurationFactory {
  private final Plugin plugin;

  public ConfigurationFactory(Plugin plugin) {
    this.plugin = plugin;
  }

  @Nullable
  public IConfiguration yaml(String path) {
    String[] split = path.split("\\.");
    if (split.length < 2) {
      return null;
    }
    if (!split[1].equals("yml")) {
      return null;
    }
    return new YamlConfiguration(path,plugin);
  }
}
