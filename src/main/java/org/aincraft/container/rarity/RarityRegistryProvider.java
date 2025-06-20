package org.aincraft.container.rarity;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.List;
import net.kyori.adventure.text.format.TextColor;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.config.IConfigurationFactory;
import org.aincraft.api.container.IRarity;
import org.aincraft.registry.IRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class RarityRegistryProvider implements Provider<IRegistry<IRarity>> {

  private final IConfiguration generalConfiguration;

  private final Plugin plugin;

  @Inject
  public RarityRegistryProvider(@Named("general")
  IConfiguration generalConfiguration, Plugin plugin) {
    this.generalConfiguration = generalConfiguration;
    this.plugin = plugin;
  }

  @Override
  public IRegistry<IRarity> get() throws IllegalArgumentException {
    Preconditions.checkArgument(generalConfiguration.contains("rarity"));
    ConfigurationSection raritySection = generalConfiguration.getConfigurationSection("rarity");
    if (raritySection == null) {
      throw new IllegalArgumentException("rarity section cannot be null");
    }
    RarityRegistry registry = new RarityRegistry();
    RarityFactory factory = new RarityFactory(plugin);
    for (String rarityKey : raritySection.getKeys(false)) {
      try {
        IRarity rarity = factory.createFromConfiguration(rarityKey,
            raritySection.getConfigurationSection(rarityKey));
        registry.register(rarity);
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().info(ex.getMessage());
      }
    }
    return registry;
  }

  private record RarityFactory(Plugin plugin) implements IConfigurationFactory<IRarity> {

    @Override
    public @NotNull IRarity createFromConfiguration(String shallowKey, ConfigurationSection section)
        throws IllegalArgumentException {
      Preconditions.checkNotNull(section);
      Preconditions.checkArgument(section.contains("base"));
      Preconditions.checkArgument(section.contains("color"));
      Preconditions.checkArgument(section.contains("priority"));
      List<Integer> colors = section.getIntegerList("color");
      if (colors.size() < 3) {
        throw new IllegalArgumentException("color must be at least size 3");
      }
      double base = section.getDouble("base");
      TextColor color = TextColor.color(colors.get(0), colors.get(1), colors.get(2));
      int priority = section.getInt("priority");
      return new Rarity(new NamespacedKey(plugin, shallowKey.toLowerCase()), color, base,
          shallowKey, priority);
    }
  }
}
