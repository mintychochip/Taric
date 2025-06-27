package org.aincraft.effects;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SharedRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public final class EffectRegistryProvider implements Provider<IRegistry<IGemEffect>> {

  private final IConfiguration gemConfiguration;
  private final IRegistry<IRarity> rarityRegistry;
  private final IRegistry<ISocketColor> colorRegistry;
  private final IRegistry<ITriggerType<?>> triggerRegistry;
  private final Plugin plugin;

  @Inject
  public EffectRegistryProvider(@Named("gems") IConfiguration gemConfiguration,
      IRegistry<IRarity> rarityRegistry, IRegistry<ISocketColor> colorRegistry,
      IRegistry<ITriggerType<?>> triggerRegistry, Plugin plugin) {
    this.gemConfiguration = gemConfiguration;
    this.rarityRegistry = rarityRegistry;
    this.colorRegistry = colorRegistry;
    this.triggerRegistry = triggerRegistry;
    this.plugin = plugin;
  }

  @Override
  public IRegistry<IGemEffect> get() {
    IRegistry<IGemEffect> registry = new SharedRegistry<IGemEffect>().register(
            Effects.AUTO_SMELT)
        .register(Effects.BURROWING)
        .register(Effects.FLARE)
        .register(Effects.VAMPIRISM)
        .register(Effects.INSIGHT)
        .register(Effects.SCAVENGE)
        .register(Effects.NETHER_SCOURGE)
        .register(Effects.VEIN_MINER)
        .register(Effects.VORPAL)
        .register(Effects.PRISMATIC)
        .register(Effects.BLINK)
        .register(Effects.FROSTBITE)
        .register(Effects.MULTISHOT)
        .register(Effects.OVERFLOWING)
        .register(Effects.HARVEST)
        .register(Effects.GLIMMER)
        .register(Effects.TILLER)
        .register(Effects.MANA_BORE)
        .register(Effects.CRUSH)
        .register(Effects.HARDENED);
    GemMetaFactory factory = new GemMetaFactory(rarityRegistry, colorRegistry, triggerRegistry,
        plugin);
    for (String gemKey : gemConfiguration.getKeys(false)) {
      try {
        Taric.getLogger().info(gemKey);
        IGemEffect gemEffect = registry.get(new NamespacedKey(plugin, gemKey));
        if (gemEffect instanceof AbstractGemEffect age) {
          age.setMeta(factory.createFromConfiguration(gemKey,
              gemConfiguration.getConfigurationSection(gemKey)));
        }
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().info(ex.getMessage());
      }
    }
    return registry;
  }
}
