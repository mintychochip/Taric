package org.aincraft.container.registerable;

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
import org.aincraft.registry.SharedRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class RarityRegistryInitializer implements Provider<IRegistry<IRarity>> {

  private final IConfiguration generalConfiguration;

  private final Plugin plugin;

  @Inject
  public RarityRegistryInitializer(@Named("general")
  IConfiguration generalConfiguration, Plugin plugin) {
    this.generalConfiguration = generalConfiguration;
    this.plugin = plugin;
  }

  private record RarityFactory(Plugin plugin) implements IConfigurationFactory<IRarity> {

    private static int priority = 0;

    @Override
    public @NotNull IRarity createFromConfiguration(String shallowKey, ConfigurationSection section)
        throws IllegalArgumentException {
      Preconditions.checkNotNull(section);
      Preconditions.checkArgument(section.contains("base"));
      Preconditions.checkArgument(section.contains("rgb") | section.contains("hex"));
      Preconditions.checkArgument(section.contains("decay-rate"));
      TextColor color;
      if (section.contains("rgb")) {
        List<Integer> rgb = section.getIntegerList("rgb");
        if (rgb.size() < 3) {
          throw new IllegalArgumentException("at least 3 rgb components");
        }
        color = TextColor.color(rgb.get(0), rgb.get(1), rgb.get(2));
      } else {
        String hexString = section.getString("hex");
        if (hexString == null) {
          throw new IllegalArgumentException("hex string cannot be null");
        }
        color = TextColor.fromHexString(hexString);
      }
      double base = section.getDouble("base");
      String name = AbstractRegisterable.toTitleCase(shallowKey);
      double decayRate = section.getDouble("decay-rate");
      return new Rarity(new NamespacedKey(plugin, shallowKey.toLowerCase()), color, base,
          name, priority++, decayRate);
    }
  }

  @Override
  public IRegistry<IRarity> get() throws IllegalArgumentException {
    Preconditions.checkArgument(generalConfiguration.contains("rarity"));
    ConfigurationSection raritySection = generalConfiguration.getConfigurationSection("rarity");
    if (raritySection == null) {
      throw new IllegalArgumentException("rarity section cannot be null");
    }
    SharedRegistry<IRarity> registry = new SharedRegistry<>();
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
}
