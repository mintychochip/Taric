package org.aincraft.container.rarity;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.config.IConfigurationFactory;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.container.util.WeightedRandomSelector;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SharedRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class IdentificationTableRegistryInitializer implements
    Provider<IRegistry<IIdentificationTable>> {

  private final IRegistry<IRarity> rarityRegistry;
  private final Plugin plugin;
  private final IConfiguration generalConfiguration;

  @Inject
  public IdentificationTableRegistryInitializer(IRegistry<IRarity> rarityRegistry, Plugin plugin,
      @Named("general") IConfiguration generalConfiguration) {
    this.rarityRegistry = rarityRegistry;
    this.plugin = plugin;
    this.generalConfiguration = generalConfiguration;
  }

  @Override
  public IRegistry<IIdentificationTable> get() {
    SharedRegistry<IIdentificationTable> registry = new SharedRegistry<>();
    IdentificationTableFactory factory = new IdentificationTableFactory(
        rarityRegistry, plugin);
    ConfigurationSection section = generalConfiguration.getConfigurationSection(
        "identification-tables");
    for (String tableKey : section.getKeys(false)) {
      ConfigurationSection tableSection = section.getConfigurationSection(tableKey);
      try {
        IIdentificationTable identificationTable = factory.createFromConfiguration(tableKey,
            tableSection);
        registry.register(identificationTable);
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().info(ex.getMessage());
      }
    }
    return registry;
  }

  static final class IdentificationTableFactory implements
      IConfigurationFactory<IIdentificationTable> {

    private final IRegistry<IRarity> rarityRegistry;
    private final Plugin plugin;

    IdentificationTableFactory(IRegistry<IRarity> rarityRegistry, Plugin plugin) {
      this.rarityRegistry = rarityRegistry;
      this.plugin = plugin;
    }

    @Override
    public @NotNull IIdentificationTable createFromConfiguration(String shallowKey,
        ConfigurationSection section) throws IllegalArgumentException {
      for (IRarity rarity : rarityRegistry) {
        String rarityString = rarity.key().value();
        Preconditions.checkArgument(section.contains(rarityString),
            "identification table: %s does not contain the rarity weight for: %s".formatted(
                shallowKey, rarityString));
      }
      WeightedRandomSelector<IRarity> randomSelector = new WeightedRandomSelector<>();
      for (IRarity rarity : rarityRegistry) {
        randomSelector.put(rarity, Math.max(0.0, section.getDouble(rarity.key().value())));
      }
      return new IdentificationTable(new NamespacedKey(plugin, shallowKey), randomSelector);
    }
  }
}
