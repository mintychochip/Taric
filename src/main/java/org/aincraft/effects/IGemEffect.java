package org.aincraft.effects;

import java.util.List;
import java.util.Set;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.IWeighable;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

public interface IGemEffect extends Keyed, IWeighable {

  int getPriority(ITriggerType<?> triggerType);

  IRarity getRarity();

  ISocketColor getSocketColor();

  boolean isValidTarget(ITriggerType<?> trigger, Material material);

  boolean isValidTarget(Material material);

  String getAdjective();

  int getMaxRank();

  Permission getPermission();

  /**
   * Returns the set of equipment slots from which the effect can be activated. If the item
   * containing the effect is not in one of these slots, the effect will not be triggered.
   */
  @NotNull
  Set<EquipmentSlot> getRequiredActiveSlots();

  boolean isValidSlot(EquipmentSlot slot);

  String getDescription();

  String getName();

  //TODO: move this to abstract effect
  default double loadDouble(IConfiguration config, String subPath, double defaultValue) {
    String path = this.key().value() + ".settings." + subPath;
    if (!config.contains(path)) {
      logMissing("Double", path, defaultValue);
      return defaultValue;
    }
    return config.getDouble(path);
  }

  static void logMissing(String type, String path, Object fallback) {
    Taric.getLogger().warning(type + " not found at '" + path + "', using default: " + fallback);
  }

  default int loadInt(IConfiguration config, String subPath, int defaultValue) {
    String path = this.key().value() + ".settings." + subPath;
    if (!config.contains(path)) {
      logMissing("Integer", path, defaultValue);
      return defaultValue;
    }
    return config.getInt(path);
  }

  default boolean loadBoolean(IConfiguration config, String subPath, boolean defaultValue) {
    String path = this.key().value() + ".settings." + subPath;
    if (!config.contains(path)) {
      logMissing("Boolean", path, defaultValue);
      return defaultValue;
    }
    return config.getBoolean(path);
  }

  default String loadString(IConfiguration config, String subPath, String defaultValue) {
    String path = this.key().value() + ".settings." + subPath;
    if (!config.contains(path)) {
      logMissing("String", path, defaultValue);
      return defaultValue;
    }
    return config.getString(path);
  }

  default List<String> loadStringList(IConfiguration config, String subPath,
      List<String> defaultValue) {
    String path = this.key().value() + ".settings." + subPath;
    if (!config.contains(path)) {
      logMissing("String List", path, defaultValue);
      return defaultValue;
    }
    List<String> list = config.getStringList(path);
    return list != null ? list : defaultValue;
  }
}
