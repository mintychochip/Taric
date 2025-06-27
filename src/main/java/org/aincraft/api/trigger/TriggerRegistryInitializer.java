package org.aincraft.api.trigger;

import com.google.inject.Provider;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SharedRegistry;

public final class TriggerRegistryInitializer implements Provider<IRegistry<ITriggerType<?>>> {

  @Override
  public IRegistry<ITriggerType<?>> get() {
    SharedRegistry<ITriggerType<?>> registry = new SharedRegistry<>();
    TriggerTypes.initialize(registry);
    return registry;
  }
}
