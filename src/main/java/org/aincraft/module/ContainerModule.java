package org.aincraft.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerRegistryInitializer;
import org.aincraft.container.GemIdentifier;
import org.aincraft.container.ItemFactoryModule;
import org.aincraft.container.context.DispatchProvider;
import org.aincraft.container.context.IDispatch;
import org.aincraft.container.distribution.DistributionModule;
import org.aincraft.container.registerable.IdentificationTableRegistryInitializer;
import org.aincraft.container.registerable.RarityRegistryInitializer;
import org.aincraft.container.registerable.SocketRegistryInitializer;
import org.aincraft.container.util.SelectorModule;
import org.aincraft.effects.EffectRegistryProvider;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;

public final class ContainerModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SelectorModule());
    install(new ItemFactoryModule());
    install(new DistributionModule());
    bind(RandomGenerator.class).toInstance(ThreadLocalRandom.current());
    bind(new TypeLiteral<IRegistry<IGemEffect>>() {
    }).toProvider(EffectRegistryProvider.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<IRarity>>() {
    }).toProvider(RarityRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<ISocketColor>>() {
    }).toProvider(SocketRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<IIdentificationTable>>() {
    }).toProvider(
        IdentificationTableRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<ITriggerType<?>>>() {
    }).toProvider(TriggerRegistryInitializer.class).in(Singleton.class);
    bind(IGemIdentifier.class).to(GemIdentifier.class).in(Singleton.class);
    bind(IDispatch.class).toProvider(DispatchProvider.class).in(Singleton.class);
  }
}
