package org.aincraft.container.dispatch;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.container.context.IContextProvider;
import org.aincraft.container.registerable.ITriggerType;
import org.bukkit.event.Event;

public interface IDispatchContext<T, C, E extends Event> {

  ITriggerType<T> triggerType();

  IContextProvider<C, E> contextProvider();

  void trigger(T trigger, C context, EffectInstanceMeta meta);
}
