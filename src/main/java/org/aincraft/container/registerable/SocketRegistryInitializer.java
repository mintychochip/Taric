package org.aincraft.container.registerable;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.List;
import net.kyori.adventure.text.format.TextColor;
import org.aincraft.Taric;
import org.aincraft.api.SocketColor;
import org.aincraft.api.config.IConfigurationFactory;
import org.aincraft.api.config.YamlConfiguration;
import org.aincraft.registry.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class SocketRegistryInitializer implements Provider<Registry<SocketColor>> {

  private final Plugin plugin;
  private final YamlConfiguration generalConfiguration;

  @Inject
  SocketRegistryInitializer(Plugin plugin,
      @Named("general") YamlConfiguration generalConfiguration) {
    this.plugin = plugin;
    this.generalConfiguration = generalConfiguration;
  }

  private record SocketColorFactory(Plugin plugin) implements IConfigurationFactory<SocketColor> {

    @Override
    public @NotNull SocketColor createFromConfiguration(String shallowKey,
        ConfigurationSection section) throws IllegalArgumentException {
      Preconditions.checkNotNull(section);
      Preconditions.checkArgument(section.contains("color"));
      List<Integer> colors = section.getIntegerList("color");
      if (colors.size() < 3) {
        throw new IllegalArgumentException("color must be at least size of 3");
      }
      TextColor color = TextColor.color(colors.get(0), colors.get(1), colors.get(2));
      String name = AbstractRegisterable.toTitleCase(shallowKey);
      return new SocketColor(new NamespacedKey(plugin, shallowKey.toLowerCase()), name,
          color);
    }
  }

  @Override
  public Registry<SocketColor> get() {
    Preconditions.checkArgument(generalConfiguration.contains("colors"));
    ConfigurationSection colors = generalConfiguration.getConfigurationSection(
        "colors");
    if (colors == null) {
      throw new IllegalArgumentException("color section cannot be null");
    }
    Registry<SocketColor> registry = Registry.simple();
    SocketColorFactory factory = new SocketColorFactory(plugin);
    for (String colorKey : colors.getKeys(false)) {
      try {
        SocketColor color = factory.createFromConfiguration(colorKey,
            colors.getConfigurationSection(colorKey));
        registry.register(color);
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().info(ex.getMessage());
      }
    }
    return registry;
  }
}
