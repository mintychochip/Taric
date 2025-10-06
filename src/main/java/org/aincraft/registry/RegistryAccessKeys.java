package org.aincraft.registry;

import net.kyori.adventure.key.Keyed;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.registry.RegistryAccess.RegistryAccessKey;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("1.0.2")
public final class RegistryAccessKeys {

  public static final RegistryAccessKey<TriggerType<?>> TRIGGER_TYPE = key("trigger_type");

  private RegistryAccessKeys() {
    throw new UnsupportedOperationException("do not instantiate");
  }

  private static <T extends Keyed> RegistryAccessKey<T> key(String key) {
    return () -> key;
  }
}
