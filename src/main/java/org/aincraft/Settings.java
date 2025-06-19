package org.aincraft;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.gems.Effects;
import org.aincraft.registry.IRegistry;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public final class Settings {

  public static final String[] REQUIRED_GEM_FIELDS = {"description", "priority",
      "max-rank", "required-active-slots"};

  public static boolean DEBUG = true;

  public static boolean ITEM_DROPS_IN_CREATIVE;
  public static boolean FAKE_EFFECTS_IN_CREATIVE;
  public static boolean ITEM_BREAK_IN_CREATIVE;
  public static boolean ITEM_TAKES_DAMAGE_IN_CREATIVE;

  public static double VAMPIRIC_FACTOR;
  public static int KNOWLEDGE_ORBS_MIN;
  public static int KNOWLEDGE_ORBS_MAX;
  public static Set<Material> SCAVENGE_BLACK_LIST;
  public static int NETHER_SCOURGE_DAMAGE_RANK_MIN;
  public static int NETHER_SCOURGE_DAMAGE_RANK_MAX;
  public static Set<EntityType> NETHER_SCOURGE_AFFECTED_TYPES;
  public static int VEIN_MINER_MAX_BLOCKS;
  public static int VEIN_MINER_DEPTH_RANK;
  public static double VORPAL_CHANCE_RANK;
  public static int COLD_ASPECT_FREEZE_TICKS_RANK;
  public static int COLD_ASPECT_MAX_FREEZE_TICKS;
  public static int MULTISHOT_PROJECTILES_RANK;

  public static void initialize() {
    IConfiguration generalConfig = Taric.getConfiguration("general");
    ConfigurationSection settings = generalConfig.getConfigurationSection("settings");

    ITEM_DROPS_IN_CREATIVE = loadBoolean(settings, "item-drops-in-creative");
    FAKE_EFFECTS_IN_CREATIVE = loadBoolean(settings, "fake-effects-in-creative");
    ITEM_BREAK_IN_CREATIVE = loadBoolean(settings, "item-break-in-creative");
    ITEM_TAKES_DAMAGE_IN_CREATIVE = loadBoolean(settings, "item-takes-damage-in-creative");

    IConfiguration gemConfig = Taric.getConfiguration("gems");
    IRegistry<IGemEffect> effects = Taric.getEffects();
    if (effects.isRegistered(Effects.VAMPIRISM.key())) {
      VAMPIRIC_FACTOR = Effects.VAMPIRISM.loadDouble(gemConfig, "factor", 1);
    }
    if (effects.isRegistered(Effects.ECHOS_OF_INSIGHT.key())) {
      KNOWLEDGE_ORBS_MIN = Effects.ECHOS_OF_INSIGHT.loadInt(gemConfig, "orbs-min", 1);
      KNOWLEDGE_ORBS_MAX = Effects.ECHOS_OF_INSIGHT.loadInt(gemConfig, "orbs-max", 1);
    }
    if (effects.isRegistered(Effects.SCAVENGE.key())) {
      SCAVENGE_BLACK_LIST = Effects.SCAVENGE.loadStringList(gemConfig, "black-list",
              new ArrayList<>()).stream().map(material -> Material.valueOf(material.toUpperCase()))
          .collect(
              Collectors.toSet());
    }
    if (effects.isRegistered(Effects.NETHER_SCOURGE.key())) {
      NETHER_SCOURGE_DAMAGE_RANK_MIN = Effects.NETHER_SCOURGE.loadInt(gemConfig,
          "damage-rank-min", 1);
      NETHER_SCOURGE_DAMAGE_RANK_MAX = Effects.NETHER_SCOURGE.loadInt(gemConfig,
          "damage-rank-max", 2);
      NETHER_SCOURGE_AFFECTED_TYPES = Effects.NETHER_SCOURGE.loadStringList(gemConfig,
              "affected-types", new ArrayList<>()).stream()
          .map(type -> EntityType.valueOf(type.toUpperCase())).collect(Collectors.toSet());
    }
    if (effects.isRegistered(Effects.VEIN_MINER.key())) {
      VEIN_MINER_MAX_BLOCKS = Effects.VEIN_MINER.loadInt(gemConfig, "max-blocks", 1);
      VEIN_MINER_DEPTH_RANK = Effects.VEIN_MINER.loadInt(gemConfig, "depth-rank", 1);
    }
    if (effects.isRegistered(Effects.VORPAL.key())) {
      VORPAL_CHANCE_RANK = Effects.VORPAL.loadDouble(gemConfig, "chance-rank", 1);
    }
    if (effects.isRegistered(Effects.COLD_ASPECT.key())) {
      COLD_ASPECT_FREEZE_TICKS_RANK = Effects.COLD_ASPECT.loadInt(gemConfig,
          "freeze-ticks-rank", 1);
      COLD_ASPECT_MAX_FREEZE_TICKS = Effects.COLD_ASPECT.loadInt(gemConfig, "max-freeze-ticks",
          1);
    }
    if (effects.isRegistered(Effects.MULTISHOT.key())) {
      MULTISHOT_PROJECTILES_RANK = Effects.MULTISHOT.loadInt(gemConfig, "arrows-rank", 1);
    }
  }

  public static boolean loadBoolean(ConfigurationSection section, String path) {
    if (!section.contains(path)) {
      Taric.getLogger().info("Boolean not found at: '" + path + "', using 'false' instead.");
    }

    return section.getBoolean(path);
  }

  public static double loadDouble(IConfiguration configuration, String path) {
    if (!configuration.contains(path)) {
      Taric.getLogger().info("Double not found at: '" + path + "', using '0.0' instead.");
    }
    return configuration.getDouble(path, 0.0);
  }

  public static int loadInt(IConfiguration configuration, String path) {
    if (!configuration.contains(path)) {
      Taric.getLogger().info("Integer not found at: '" + path + "', using '0' instead.");
    }
    return configuration.getInt(path, 0);
  }

  public static double loadDouble(ConfigurationSection section, String path, double def) {
    if (!section.contains(path)) {
      Taric.getLogger().info("Double not found at: '" + path + "', using '" + def + "' instead.");
    }
    return section.getDouble(path, def);
  }

  private static boolean isNotInCreative(Player player) {
    return player.getGameMode() != GameMode.CREATIVE;
  }

  public static boolean fakeEffectsShouldPlay(Player player) {

    return Settings.FAKE_EFFECTS_IN_CREATIVE || isNotInCreative(player);
  }

  public static boolean itemsShouldDrop(Player player) {
    return Settings.ITEM_DROPS_IN_CREATIVE || isNotInCreative(player);
  }

  public static boolean itemShouldBreak(Player player) {
    return Settings.ITEM_BREAK_IN_CREATIVE || isNotInCreative(player);
  }

  public static boolean itemShouldTakeDamage(Player player) {
    return Settings.ITEM_TAKES_DAMAGE_IN_CREATIVE || isNotInCreative(player);
  }
}
