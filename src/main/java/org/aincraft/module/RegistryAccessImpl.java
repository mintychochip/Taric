package org.aincraft.module;

import com.google.inject.Inject;
import net.kyori.adventure.key.Keyed;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.NotNull;

final class RegistryAccessImpl implements RegistryAccess {

  private final Registry<TriggerType<?>> triggerTypeRegistry;

  @Inject
  RegistryAccessImpl(Registry<TriggerType<?>> triggerTypeRegistry) {
    this.triggerTypeRegistry = triggerTypeRegistry;
  }


  @SuppressWarnings("unchecked")
  @Override
  public @NotNull <T extends Keyed> Registry<T> getRegistry(RegistryAccessKey<T> registryAccessKey)
      throws IllegalStateException {
    return switch (registryAccessKey.getKey()) {
      case "trigger_type" -> (Registry<T>) triggerTypeRegistry;
      default -> throw new IllegalStateException("Unexpected value: " + registryAccessKey.getKey());
    };
  }
}
