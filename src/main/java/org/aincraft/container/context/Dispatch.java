package org.aincraft.container.context;

import java.util.function.Consumer;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.effects.IGemEffect;
import org.bukkit.event.Event;

final class Dispatch implements IDispatch {

  private final EffectQueuePool poolMap = new EffectQueuePool();

  @Override
  public <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle) {
    return this.dispatch(context, loader, handle, null);
  }

  @Override
  public <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle, Consumer<E> eventConsumer) {
    ITriggerType<T> triggerType = context.triggerType();
    C eventContext = context.contextProvider().create(handle);
    EffectQueue queue = this.poolMap.acquire(triggerType, loader);
    if (!queue.isEmpty()) {
      if (eventConsumer != null) {
        eventConsumer.accept(handle);
      }
      Class<T> clazz = triggerType.getTriggerClazz();
      for (IEffectInstance instance : queue) {
        IGemEffect effect = instance.getEffect();
        if (clazz.isInstance(effect)) {
          T cast = clazz.cast(effect);
          EffectInstanceMeta meta = instance.getMeta();
          context.trigger(cast, eventContext, meta);
        }
      }
    }
    this.poolMap.release(queue);
    return eventContext;
  }

  @Override
  public <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IGemInventory inventory, E handle, Consumer<E> eventConsumer) {
    IEffectQueueLoader loader = inventory.getLoader(context.triggerType());
    return this.dispatch(context, loader, handle, eventConsumer);
  }

  @Override
  public <T, C, E extends Event> C dispatch(IDispatchContext<T, C, E> context,
      IGemInventory inventory, E handle) {
    return this.dispatch(context, inventory, handle, null);
  }
}
