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
import org.aincraft.api.container.ISocketColor;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SharedRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class SocketRegistryInitializer implements Provider<IRegistry<ISocketColor>> {

  private final Plugin plugin;
  private final IConfiguration generalConfiguration;

  @Inject
  SocketRegistryInitializer(Plugin plugin, @Named("general") IConfiguration generalConfiguration) {
    this.plugin = plugin;
    this.generalConfiguration = generalConfiguration;
  }

  @Override
  public IRegistry<ISocketColor> get() {
    Preconditions.checkArgument(generalConfiguration.contains("colors"));
    ConfigurationSection colors = generalConfiguration.getConfigurationSection(
        "colors");
    if (colors == null) {
      throw new IllegalArgumentException("color section cannot be null");
    }
    SharedRegistry<ISocketColor> registry = new SharedRegistry<>();
    SocketColorFactory factory = new SocketColorFactory(plugin);
    for (String colorKey : colors.getKeys(false)) {
      try {
        ISocketColor color = factory.createFromConfiguration(colorKey,
            colors.getConfigurationSection(colorKey));
        registry.register(color);
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().info(ex.getMessage());
      }
    }
    return registry;
  }

  private record SocketColorFactory(Plugin plugin) implements IConfigurationFactory<ISocketColor> {

    @Override
    public @NotNull ISocketColor createFromConfiguration(String shallowKey,
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
}
