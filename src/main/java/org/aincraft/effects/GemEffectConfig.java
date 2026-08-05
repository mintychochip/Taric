package org.aincraft.effects;

import java.util.List;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;

/**
 * Loads per-effect settings from gem configuration using the effect key as the path root.
 * Lives outside {@link IGemEffect} so the contract stays free of config-loading defaults.
 */
public final class GemEffectConfig {

  private GemEffectConfig() {
  }

  public static double loadDouble(IGemEffect effect, IConfiguration config, String subPath,
      double defaultValue) {
    String path = settingsPath(effect, subPath);
    if (!config.contains(path)) {
      logMissing("Double", path, defaultValue);
      return defaultValue;
    }
    return config.getDouble(path);
  }

  public static int loadInt(IGemEffect effect, IConfiguration config, String subPath,
      int defaultValue) {
    String path = settingsPath(effect, subPath);
    if (!config.contains(path)) {
      logMissing("Integer", path, defaultValue);
      return defaultValue;
    }
    return config.getInt(path);
  }

  public static boolean loadBoolean(IGemEffect effect, IConfiguration config, String subPath,
      boolean defaultValue) {
    String path = settingsPath(effect, subPath);
    if (!config.contains(path)) {
      logMissing("Boolean", path, defaultValue);
      return defaultValue;
    }
    return config.getBoolean(path);
  }

  public static String loadString(IGemEffect effect, IConfiguration config, String subPath,
      String defaultValue) {
    String path = settingsPath(effect, subPath);
    if (!config.contains(path)) {
      logMissing("String", path, defaultValue);
      return defaultValue;
    }
    return config.getString(path);
  }

  public static List<String> loadStringList(IGemEffect effect, IConfiguration config,
      String subPath, List<String> defaultValue) {
    String path = settingsPath(effect, subPath);
    if (!config.contains(path)) {
      logMissing("String List", path, defaultValue);
      return defaultValue;
    }
    List<String> list = config.getStringList(path);
    return list != null ? list : defaultValue;
  }

  private static String settingsPath(IGemEffect effect, String subPath) {
    return effect.key().value() + ".settings." + subPath;
  }

  private static void logMissing(String type, String path, Object fallback) {
    Taric.getLogger().warning(type + " not found at '" + path + "', using default: " + fallback);
  }
}
