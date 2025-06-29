package org.aincraft.container;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.aincraft.api.container.IEquipment.IEquipmentFactory;
import org.aincraft.api.container.gem.IGemInventoryFactory;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemFactory;
import org.aincraft.container.SocketGem.SocketGemFactory;
import org.aincraft.container.UnidentifiedGem.UnidentifiedGemFactory;

public final class ItemFactoryModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ISocketGemFactory.class).to(SocketGemFactory.class).in(Singleton.class);
    bind(IGemItemFactory.class).to(GemItem.Factory.class).in(Singleton.class);
    bind(IUnidentifiedGemFactory.class).to(UnidentifiedGemFactory.class).in(Singleton.class);
    bind(IEquipmentFactory.class).to(EquipmentFactory.class).in(Singleton.class);
    bind(IGemInventoryFactory.class).to(GemInventory.GemInventoryFactory.class).in(Singleton.class);
  }
}
