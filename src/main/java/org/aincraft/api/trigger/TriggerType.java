package org.aincraft.api.trigger;

import net.kyori.adventure.key.Keyed;

public interface TriggerType<T> extends Keyed {

  Class<T> triggerClazz();

}
