package org.aincraft.container.context;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.event.Event;

public interface IDispatchContext<T, C, E extends Event> {

  ITriggerType<T> triggerType();

  IContextProvider<C, E> contextProvider();

  void trigger(T trigger, C context, EffectInstanceMeta meta);
}
