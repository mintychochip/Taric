package org.aincraft.trigger;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.registry.Registry;

public final class TriggerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(new TypeLiteral<Registry<TriggerType<?>>>() {
    }).toProvider(TriggerTypeRegistryProvider.class);
  }
}
