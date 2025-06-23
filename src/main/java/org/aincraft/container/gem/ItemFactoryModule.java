package org.aincraft.container.gem;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.aincraft.api.container.gem.IGem.IGemFactory;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;

public final class ItemFactoryModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(IGemFactory.class).to(Gem.Factory.class).in(Singleton.class);
    bind(IGemItemFactory.class).to(GemItem.Factory.class).in(Singleton.class);
    bind(IPreciousGemFactory.class).to(PreciousGem.Factory.class).in(Singleton.class);
  }
}
