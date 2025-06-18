package org.aincraft.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.util.Map;
import java.util.Map.Entry;
import org.aincraft.Taric;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.config.IConfiguration;
import org.bukkit.plugin.Plugin;

public final class PluginModule extends AbstractModule {

  private final Plugin plugin;
  private final Map<String, String> configs;

  public PluginModule(Plugin plugin, Map<String, String> configs) {
    this.plugin = plugin;
    this.configs = configs;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(Taric.class).asEagerSingleton();
    ConfigurationFactory configurationFactory = new ConfigurationFactory(plugin);
    for (Entry<String, String> entry : configs.entrySet()) {
      bind(IConfiguration.class).annotatedWith(Names.named(entry.getKey()))
          .toInstance(configurationFactory.yaml(entry.getValue()));
    }
  }
}