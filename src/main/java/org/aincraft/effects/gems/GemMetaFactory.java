package org.aincraft.effects.gems;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.aincraft.api.config.IConfigurationFactory;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.effects.gems.AbstractGemEffect.GemEffectMeta;
import org.aincraft.registry.IRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

final class GemMetaFactory implements IConfigurationFactory<GemEffectMeta> {

  private final IRegistry<IRarity> rarityRegistry;
  private final Plugin plugin;

  @Inject
  GemMetaFactory(IRegistry<IRarity> rarityRegistry, Plugin plugin) {
    this.rarityRegistry = rarityRegistry;
    this.plugin = plugin;
  }

  private static final String[] REQUIRED_FIELDS = {"max-rank", "rarity", "priority",
      "description", "required-active-slots"};

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
    IRarity rarity = rarityRegistry.get(new NamespacedKey(plugin, rarityString));
    ConfigurationSection prioritySection = section.getConfigurationSection("priority");
    if (prioritySection == null) {
      throw new IllegalArgumentException("priority section cannot be null");
    }
    Map<TriggerType, Integer> priorityMap = getPriorityMap(prioritySection);
    String description = section.getString("description");
    Set<EquipmentSlot> slots = section.getStringList("required-active-slots").stream()
        .map(EquipmentSlot::valueOf).collect(
            Collectors.toSet());
    return new GemEffectMeta(maxRank, rarity, priorityMap, description, slots);
  }

  private static Map<TriggerType, Integer> getPriorityMap(@NotNull ConfigurationSection section)
      throws IllegalArgumentException {
    Map<TriggerType, Integer> priorityMap = new HashMap<>();
    for (String triggerTypeString : section.getKeys(false)) {
      priorityMap.put(TriggerType.valueOf(triggerTypeString.toUpperCase()),
          section.getInt(triggerTypeString));
    }
    return priorityMap;
  }
}
