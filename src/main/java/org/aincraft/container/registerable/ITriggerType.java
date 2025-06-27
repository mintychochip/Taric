package org.aincraft.container.registerable;

import net.kyori.adventure.key.Keyed;

public interface ITriggerType<T> extends Keyed {

  Class<T> getTriggerClazz();

}
