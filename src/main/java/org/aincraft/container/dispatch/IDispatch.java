package org.aincraft.container.dispatch;

import org.bukkit.event.Event;

public interface IDispatch {

  <T, C, E extends Event> void dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle);
}
