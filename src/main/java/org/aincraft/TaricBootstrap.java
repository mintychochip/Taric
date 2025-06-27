package org.aincraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Map;
import org.aincraft.module.ContainerModule;
import org.aincraft.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaricBootstrap extends JavaPlugin {

  static final Map<String, String> CONFIGS = Map.ofEntries(
      Map.entry("general", "general.yml"),
      Map.entry("gems", "gems.yml"),
      Map.entry("db", "db.yml"),
      Map.entry("distribution", "distribution.yml")
  );
  private static Taric application;

  public static Taric getApplication() {
    return application;
  }

  @Override
  public void onDisable() {
    if (application == null) {
      return;
    }
    application.disable();
  }

  @Override
  public void onEnable() {
    Injector injector = Guice.createInjector(new PluginModule(this, CONFIGS),
        new ContainerModule());
    application = injector.getInstance(Taric.class);
    if (application == null) {
      return;
    }
    application.enable();
  }
}
