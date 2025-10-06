package org.aincraft.container.util;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.aincraft.api.Rarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.registry.IRegistry;

public final class SelectorModule extends AbstractModule {

  static final class RaritySelectorInitializer implements Provider<IRandomSelector<Rarity>> {

    private final IRegistry<Rarity> rarityRegistry;

    @Inject
    public RaritySelectorInitializer(IRegistry<Rarity> rarityRegistry) {
      this.rarityRegistry = rarityRegistry;
    }

    @Override
    public IRandomSelector<Rarity> get() {
      WeightedRandomSelector<Rarity> randomSelector = new WeightedRandomSelector<>();
      rarityRegistry.forEach(rarity -> {
        randomSelector.put(rarity, rarity.weight());
      });
      return randomSelector;
    }
  }

  static final class ColorSelectorInitializer implements Provider<IRandomSelector<ISocketColor>> {

    private final IRegistry<ISocketColor> colorRegistry;

    @Inject
    ColorSelectorInitializer(IRegistry<ISocketColor> colorRegistry) {
      this.colorRegistry = colorRegistry;
    }

    @Override
    public IRandomSelector<ISocketColor> get() {
      UniformRandomSelector<ISocketColor> randomSelector = new UniformRandomSelector<>();
      colorRegistry.forEach(randomSelector::add);
      return randomSelector;
    }
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<IRandomSelector<Rarity>>() {
    }).annotatedWith(Names.named("rarity-selector"))
        .toProvider(SelectorModule.RaritySelectorInitializer.class)
        .in(Singleton.class);
    bind(new TypeLiteral<IRandomSelector<ISocketColor>>() {
    }).annotatedWith(Names.named("color-selector"))
        .toProvider(SelectorModule.ColorSelectorInitializer.class)
        .in(Singleton.class);
  }

}
