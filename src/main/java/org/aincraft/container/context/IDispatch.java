package org.aincraft.container.context;

import java.util.function.Consumer;
import org.aincraft.api.container.gem.IGemInventory;
import org.bukkit.event.Event;

public interface IDispatch {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle);

  <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle, Consumer<E> eventConsumer);

  <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IGemInventory inventory, E handle, Consumer<E> eventConsumer);

  <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IGemInventory inventory, E handle);
}
