package org.aincraft.module;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import java.time.Duration;
import org.aincraft.container.GemInventory;
import org.aincraft.database.Extractor;
import org.aincraft.database.Extractor.ResourceExtractor;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SimpleRegistry;
import org.bukkit.entity.Entity;

public class RuntimeModule extends AbstractModule {

  private static LoadingCache<Entity, GemInventory> createPlayerCache() {
    return CacheBuilder.newBuilder().expireAfterWrite(
        Duration.ofMinutes(5)).build(CacheLoader.from(GemInventory::from));
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<IRegistry<String, IGemEffect>>() {
    }).toInstance(new SimpleRegistry<>());
    bind(new TypeLiteral<LoadingCache<Entity, GemInventory>>() {
    }).toInstance(createPlayerCache());
    bind(EffectQueuePool.class).toInstance(new EffectQueuePool<>());
    bind(IDatabase.class).toProvider(StorageProvider.class).in(Singleton.class);
    bind(Extractor.class).to(ResourceExtractor.class);
  }
}
