package org.aincraft.api.config;

import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

public interface IConfiguration {
  void reload();
  void save();
  boolean contains(String path);
  Set<String> getKeys(boolean deep);
  String getString(String path);
  String getString(String path, String def);
  int getInt(String path);
  int getInt(String path, int def);
  boolean getBoolean(String path);
  boolean getBoolean(String path, boolean def);
  double getDouble(String path);
  double getDouble(String path, double def);
  List<String> getStringList(String path);
  ConfigurationSection getConfigurationSection(String path);
}
