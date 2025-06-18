package org.aincraft.listeners;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class HandlerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Shared.class).in(Singleton.class);
    bind(EffectListener.class).in(Singleton.class);
  }
}
