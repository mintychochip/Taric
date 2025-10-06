package org.aincraft.effects;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.aincraft.Taric;
import org.aincraft.api.Rarity;
import org.aincraft.api.config.IConfigurationFactory;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.effects.AbstractGemEffect.GemEffectMeta;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

final class GemMetaFactory implements IConfigurationFactory<GemEffectMeta> {

  private static final String[] REQUIRED_FIELDS = {"max-rank", "adjectives", "rarity", "color",
      "priority",
      "description", "required-active-slots"};
  private final IRegistry<Rarity> rarityRegistry;
  private final IRegistry<ISocketColor> colorRegistry;
  private final Registry<TriggerType<?>> triggerRegistry;
  private final Plugin plugin;

  GemMetaFactory(IRegistry<Rarity> rarityRegistry, IRegistry<ISocketColor> colorRegistry,
      Registry<TriggerType<?>> triggerRegistry,
      Plugin plugin) {
    this.rarityRegistry = rarityRegistry;
    this.colorRegistry = colorRegistry;
    this.triggerRegistry = triggerRegistry;
    this.plugin = plugin;
  }

  @Override
  public @NotNull GemEffectMeta createFromConfiguration(String shallowKey,
      ConfigurationSection section) throws IllegalArgumentException {
    for (String field : REQUIRED_FIELDS) {
      Preconditions.checkArgument(section.contains(field));
    }
    int maxRank = section.getInt("max-rank");
    String rarityString = section.getString("rarity");
    if (rarityString == null) {
      throw new IllegalArgumentException("rarity cannot be null");
    }
    String colorString = section.getString("color");
    if (colorString == null) {
      throw new IllegalArgumentException("color cannot be null");
    }
    List<String> adjectives = section.getStringList("adjectives");
    Rarity rarity = rarityRegistry.get(new NamespacedKey(plugin, rarityString));
    ISocketColor color = colorRegistry.get(new NamespacedKey(plugin, colorString));
    ConfigurationSection prioritySection = section.getConfigurationSection("priority");
    if (prioritySection == null) {
      throw new IllegalArgumentException("priority section cannot be null");
    }
    Map<TriggerType<?>, Integer> priorityMap = getPriorityMap(prioritySection);
    Taric.getLogger().info(priorityMap.toString());
    String description = section.getString("description");
    Set<EquipmentSlot> slots = section.getStringList("required-active-slots").stream()
        .map(EquipmentSlot::valueOf).collect(
            Collectors.toSet());
    return new GemEffectMeta(maxRank, rarity, color, adjectives, priorityMap, description, slots);
  }

  private Map<TriggerType<?>, Integer> getPriorityMap(@NotNull ConfigurationSection section)
      throws IllegalArgumentException {
    Map<TriggerType<?>, Integer> priorityMap = new HashMap<>();
    for (String triggerTypeString : section.getKeys(false)) {
      String lowerCase = triggerTypeString.toLowerCase();
      NamespacedKey key = new NamespacedKey(plugin, lowerCase);
      TriggerType<?> trigger = triggerRegistry.get(key);
      if (trigger == null) {
        Taric.getLogger().info("could not find a trigger for %s".formatted(triggerTypeString));
      }
      priorityMap.put(trigger,
          section.getInt(triggerTypeString));
    }
    return priorityMap;
  }
}
