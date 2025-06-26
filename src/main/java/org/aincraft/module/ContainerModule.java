package org.aincraft.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.container.GemIdentifier;
import org.aincraft.container.ItemFactoryModule;
import org.aincraft.container.rarity.IdentificationTableRegistryInitializer;
import org.aincraft.container.rarity.RarityRegistryInitializer;
import org.aincraft.container.rarity.SocketRegistryInitializer;
import org.aincraft.container.util.SelectorModule;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.EffectRegistryProvider;
import org.aincraft.registry.IRegistry;

public final class ContainerModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SelectorModule());
    install(new ItemFactoryModule());
    bind(new TypeLiteral<IRegistry<IGemEffect>>() {
    }).toProvider(EffectRegistryProvider.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<IRarity>>() {
    }).toProvider(RarityRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<ISocketColor>>() {
    }).toProvider(SocketRegistryInitializer.class).in(Singleton.class);
    bind(new TypeLiteral<IRegistry<IIdentificationTable>>() {
    }).toProvider(
        IdentificationTableRegistryInitializer.class).in(Singleton.class);
    bind(IGemIdentifier.class).to(GemIdentifier.class).in(Singleton.class);
  }
}
