package org.aincraft.effects;

import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.aincraft.Taric;
import org.aincraft.config.IConfiguration;
import org.aincraft.container.Rarity;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public interface IGemEffect extends Keyed {

  int getPriority(TriggerType triggerType);

  Rarity getRarity();

  boolean isValidTarget(TriggerType triggerType, Material material);

  boolean isValidTarget(Material material);

  int getMaxRank();

  /**
   * Returns the set of equipment slots from which the effect can be activated. If the item
   * containing the effect is not in one of these slots, the effect will not be triggered.
   */
  @NotNull
  Set<EquipmentSlot> getRequiredActiveSlots();

  boolean isValidSlot(EquipmentSlot slot);

  TriggerType[] getTriggerTypes();

  Component getDescription();

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

  static void logMissing(String type, String path, Object fallback) {
    Taric.getLogger().warning(type + " not found at '" + path + "', using default: " + fallback);
  }
}
