package org.aincraft.module;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import java.time.Duration;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.container.rarity.RarityRegistryInitializer;
import org.aincraft.container.rarity.SocketRegistryInitializer;
import org.aincraft.container.rework.GemInventory;
import org.aincraft.database.Extractor;
import org.aincraft.database.Extractor.ResourceExtractor;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.gems.EffectRegistryProvider;
import org.aincraft.registry.IRegistry;
import org.bukkit.entity.LivingEntity;

public class RuntimeModule extends AbstractModule {

  private static LoadingCache<LivingEntity, IGemInventory> createPlayerCache() {
    return CacheBuilder.newBuilder().expireAfterWrite(
        Duration.ofMinutes(5)).build(CacheLoader.from(GemInventory::from));
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<IRegistry<IGemEffect>>() {
    }).toProvider(EffectRegistryProvider.class).in(Singleton.class);
    bind(new TypeLiteral<LoadingCache<LivingEntity, IGemInventory>>() {
    }).toInstance(createPlayerCache());
    bind(new TypeLiteral<IRegistry<IRarity>>() {
    }).toProvider(RarityRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<ISocketColor>>() {
    }).toProvider(SocketRegistryInitializer.class).in(Singleton.class);
    bind(EffectQueuePool.class).toInstance(new EffectQueuePool<>());
    bind(IDatabase.class).toProvider(StorageProvider.class).in(Singleton.class);
    bind(Extractor.class).to(ResourceExtractor.class);
  }
}
