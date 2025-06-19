package org.aincraft.api.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface IConfigurationFactory<T> {

  @NotNull
  T createFromConfiguration(String shallowKey, ConfigurationSection section)
      throws IllegalArgumentException;
}
