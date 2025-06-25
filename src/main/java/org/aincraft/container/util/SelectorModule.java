package org.aincraft.container.util;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.registry.IRegistry;

public final class SelectorModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(new TypeLiteral<IRandomSelector<IRarity>>() {
    }).toProvider(SelectorModule.RaritySelectorInitializer.class)
        .in(Singleton.class);
    bind(new TypeLiteral<IRandomSelector<ISocketColor>>() {
    }).toProvider(SelectorModule.ColorSelectorInitializer.class)
        .in(Singleton.class);
  }

  static final class RaritySelectorInitializer implements Provider<IRandomSelector<IRarity>> {

    private final IRegistry<IRarity> rarityRegistry;

    @Inject
    public RaritySelectorInitializer(IRegistry<IRarity> rarityRegistry) {
      this.rarityRegistry = rarityRegistry;
    }

    @Override
    public IRandomSelector<IRarity> get() {
      WeightedRandomSelector<IRarity> randomSelector = new WeightedRandomSelector<>();
      rarityRegistry.forEach(rarity -> {
        randomSelector.put(rarity, rarity.getWeight());
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

}
