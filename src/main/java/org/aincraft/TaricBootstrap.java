package org.aincraft;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.Map;
import org.aincraft.container.gem.ItemFactoryModule;
import org.aincraft.module.PluginModule;
import org.aincraft.module.RuntimeModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaricBootstrap extends JavaPlugin {

  private static Taric application;

  static final Map<String, String> CONFIGS = Map.ofEntries(
      Map.entry("general", "general.yml"),
      Map.entry("gems", "gems.yml"),
      Map.entry("db", "db.yml")
  );

  @Override
  public void onEnable() {
    Injector injector = Guice.createInjector(new PluginModule(this, CONFIGS), new RuntimeModule(),
        new ItemFactoryModule());
    application = injector.getInstance(Taric.class);
    if (application == null) {
      return;
    }
    application.enable();
  }

  @Override
  public void onDisable() {
    if (application == null) {
      return;
    }
    application.disable();
  }

  public static Taric getApplication() {
    return application;
  }
}
