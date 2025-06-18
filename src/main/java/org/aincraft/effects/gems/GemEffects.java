package org.aincraft.effects.gems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentDecoder;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.config.IConfiguration;
import org.aincraft.container.Rarity;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.gems.AbstractGemEffect.GemEffectMeta;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.registry.IRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public final class GemEffects {

  private static final ComponentDecoder<String, Component> COMPONENT_DECODER = new ComponentDecoder<>() {
    @Override
    public @NotNull Component deserialize(@NotNull String input) {
      return MiniMessage.miniMessage().deserialize(input);
    }
  };

  public static IGemEffect AUTO_SMELT;

  public static IGemEffect BURROWING;

  public static IGemEffect VAMPIRISM;

  public static IGemEffect ECHOS_OF_INSIGHT;

  public static IGemEffect SCAVENGE;

  public static IGemEffect VORPAL;

  public static IGemEffect NETHER_SCOURGE;

  public static IGemEffect VEIN_MINER;

  public static IGemEffect BLINK;

  //TODO: add this back, but revise the mob AI
//  public static IGemEffect MIND_CONTROL;

  public static IGemEffect COLD_ASPECT;

  public static IGemEffect MULTISHOT;

  public static IGemEffect FLARE;

  public static IGemEffect PRISMATIC;

  public static void registerEffects() {
    IRegistry<IGemEffect> effects = Taric.getEffects();
    if (effects == null) {
      return;
    }
    IConfiguration gemConfiguration = Taric.getConfiguration("gems");
    AUTO_SMELT = AutoSmelt.create(gemConfiguration);
    BURROWING = new Burrowing();
    VAMPIRISM = new Vampirism();
    ECHOS_OF_INSIGHT = new Insight();
    SCAVENGE = new Scavenge();
    NETHER_SCOURGE = new NetherScourge();
    VEIN_MINER = new VeinMiner();
    VORPAL = new Vorpal();
    BLINK = new Blink();
    COLD_ASPECT = new ColdAspect();
//    MIND_CONTROL = new MindControl("mind-control");
    MULTISHOT = new Multishot();
    FLARE = new Flare();
    PRISMATIC = new Prismatic();
    effects.register(AUTO_SMELT)
        .register(BURROWING)
        .register(FLARE)
        .register(VAMPIRISM)
        .register(ECHOS_OF_INSIGHT)
        .register(SCAVENGE)
        .register(NETHER_SCOURGE)
        .register(VEIN_MINER)
        .register(VORPAL)
        .register(PRISMATIC)
//        .register(MIND_CONTROL)
        .register(BLINK)
        .register(COLD_ASPECT)
        .register(MULTISHOT);
    for (IGemEffect effect : effects.values()) {
      if (effect instanceof AbstractGemEffect abstractGemEffect) {
        initializeCommonFields(abstractGemEffect, gemConfiguration);
      }
    }
  }

  private static void initializeCommonFields(AbstractGemEffect gemEffect,
      IConfiguration gemConfiguration) {
    ConfigurationSection section = gemConfiguration.getSection(gemEffect.key().value());
    if (section == null) {
      throw new IllegalStateException(
          "missing configuration section for gem effect: " + gemEffect.key().value());
    }

    List<String> missingFields = new ArrayList<>();
    for (String commonField : Settings.REQUIRED_GEM_FIELDS) {
      if (!section.contains(commonField)) {
        missingFields.add(commonField);
      }
    }
    if (!missingFields.isEmpty()) {
      throw new IllegalStateException(
          "missing fields for gem effect: " + gemEffect.key().value() + " : " + missingFields);
    }

    Set<EquipmentSlot> requiredActiveSlots = section.getStringList("required-active-slots")
        .stream().map(EquipmentSlot::valueOf).collect(Collectors.toSet());

    ConfigurationSection priority = section.getConfigurationSection("priority");
    if (priority == null) {
      Taric.getLogger().info("initializing gem: " + gemEffect.key().value()
          + " with an empty priority map, consider initializing it manually to prevent undefined behavior.");
    }
    gemEffect.setMeta(new GemEffectMeta(section.getInt("max-rank"),
        Rarity.valueOf(section.getString("rarity", Rarity.COMMON.toString())),
        priority != null ? loadPriorityMap(priority) : new HashMap<>(),
        section.getComponent("description", COMPONENT_DECODER),
        requiredActiveSlots
    ));
  }

  private static Map<TriggerType, Integer> loadPriorityMap(@NotNull ConfigurationSection section) {
    Map<TriggerType, Integer> priorityMap = new HashMap<>();
    for (String triggerTypeString : section.getKeys(false)) {
      try {
        TriggerType triggerType = TriggerType.valueOf(triggerTypeString.toUpperCase());
        int priority = section.getInt(triggerTypeString);
        priorityMap.put(triggerType, priority);
      } catch (IllegalArgumentException ignored) {

      }
    }
    return priorityMap;
  }
}
