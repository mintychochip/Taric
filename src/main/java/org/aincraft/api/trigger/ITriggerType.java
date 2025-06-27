package org.aincraft.api.trigger;

import net.kyori.adventure.key.Keyed;

public interface ITriggerType<T> extends Keyed {

  Class<T> getTriggerClazz();

}
