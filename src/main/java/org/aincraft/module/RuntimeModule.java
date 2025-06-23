package org.aincraft.module;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import java.time.Duration;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.gem.IGemInventoryFactory;
import org.aincraft.effects.EffectQueuePool;
import org.bukkit.entity.LivingEntity;

public class RuntimeModule extends AbstractModule {

  private final IGemInventoryFactory factory;

  @Inject
  public RuntimeModule(IGemInventoryFactory factory) {
    this.factory = factory;
  }

  private LoadingCache<LivingEntity, IGemInventory> createPlayerCache() {
    return CacheBuilder.newBuilder().expireAfterWrite(
        Duration.ofMinutes(5)).build(CacheLoader.from(factory::create));
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<LoadingCache<LivingEntity, IGemInventory>>() {
    }).toInstance(createPlayerCache());
    bind(EffectQueuePool.class).toInstance(new EffectQueuePool<>());
  }
}
