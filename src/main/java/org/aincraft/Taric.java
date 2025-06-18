package org.aincraft;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Logger;
import org.aincraft.commands.GemCommand;
import org.aincraft.commands.ToItem;
import org.aincraft.config.IConfiguration;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.gems.GemEffects;
import org.aincraft.listeners.EffectListener;
import org.aincraft.listeners.FakeEventListener;
import org.aincraft.listeners.GemCacheListener;
import org.aincraft.listeners.HandlerModule;
import org.aincraft.module.RuntimeModule;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class Taric {

  private static Taric instance;
  private final Injector injector;

  @Inject
  public Taric(Injector injector) {
    this.injector = injector.createChildInjector(new RuntimeModule(), new HandlerModule());
    instance = this;
  }

  void enable() {
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(EffectListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(GemCacheListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(FakeEventListener.class), Taric.getPlugin());
    //temporary
    Bukkit.getPluginCommand("item").setExecutor(new ToItem());
    Bukkit.getPluginCommand("gem").setExecutor(new GemCommand());
    GemEffects.registerEffects();
    Settings.initialize();
  }

  void disable() {
    IDatabase database = Taric.getDatabase();
    try {
      database.shutdown();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  void registerDefaults() {
    Taric.getEffects()
        .register(GemEffects.BURROWING)
        .register(GemEffects.AUTO_SMELT)
        .register(GemEffects.VAMPIRISM);

  }

  public static Taric getInstance() {
    return instance;
  }

  public static Plugin getPlugin() {
    return instance.injector.getInstance(Plugin.class);
  }

  public static IRegistry<IGemEffect> getEffects() {
    return instance.injector.getInstance(new Key<>() {
    });
  }

  public static Gson getGson() {
    return instance.injector.getInstance(Gson.class);
  }

  public static Random getRandom() {
    return instance.injector.getInstance(Random.class);
  }


  public static Injector getPluginInjector() {
    return instance.injector;
  }

  public static Logger getLogger() {
    return Taric.getPlugin().getLogger();
  }

  public static IDatabase getDatabase() {
    return instance.injector.getInstance(IDatabase.class);
  }

  @NotNull
  public static IConfiguration getConfiguration(String configurationKey)
      throws IllegalArgumentException {
    if (!TaricBootstrap.CONFIGS.containsKey(configurationKey)) {
      throw new IllegalArgumentException(
          "Configuration key: " + configurationKey + " is not a valid configuration key!");
    }
    return instance.injector.getInstance(
        Key.get(IConfiguration.class, Names.named(configurationKey)));
  }
}
