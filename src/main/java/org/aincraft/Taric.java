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
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.Rarities;
import org.aincraft.api.container.SocketColors;
import org.aincraft.commands.ContainerCommand;
import org.aincraft.commands.GemCommand;
import org.aincraft.container.distribution.DistributionListener;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.IGemEffect;
import org.aincraft.listeners.EffectListener;
import org.aincraft.listeners.FakeEventListener;
import org.aincraft.listeners.GemCacheListener;
import org.aincraft.listeners.GeodeListener;
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
  private final IRegistry<IRarity> rarityRegistry;
  private final IRegistry<ISocketColor> socketColorRegistry;

  @Inject
  public Taric(Injector injector, IRegistry<IRarity> rarityRegistry,
      IRegistry<ISocketColor> socketColorRegistry) {
    RuntimeModule module = injector.getInstance(RuntimeModule.class);
    this.injector = injector.createChildInjector(new HandlerModule(), module);
    this.rarityRegistry = rarityRegistry;
    this.socketColorRegistry = socketColorRegistry;
    instance = this;
  }

  public static Taric getInstance() {
    return instance;
  }

  public static IRegistry<IGemEffect> getEffects() {
    return instance.injector.getInstance(new Key<>() {
    });
  }

  public static IRegistry<IRarity> getRarity() {
    return instance.injector.getInstance(new Key<>() {
    });
  }

  public static IRegistry<ISocketColor> getColors() {
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

  public static Plugin getPlugin() {
    return instance.injector.getInstance(Plugin.class);
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

  void enable() {
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(EffectListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(GemCacheListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(FakeEventListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(GeodeListener.class), Taric.getPlugin());
    Bukkit.getPluginManager()
        .registerEvents(injector.getInstance(DistributionListener.class), Taric.getPlugin());
    //temporary
    Bukkit.getPluginCommand("container").setExecutor(injector.getInstance(ContainerCommand.class));
    Bukkit.getPluginCommand("gem").setExecutor(injector.getInstance(GemCommand.class));
    Rarities.initialize(rarityRegistry);
    SocketColors.initialize(socketColorRegistry);
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

  public static IDatabase getDatabase() {
    return instance.injector.getInstance(IDatabase.class);
  }
}
