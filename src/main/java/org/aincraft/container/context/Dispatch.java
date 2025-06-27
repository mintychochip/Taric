package org.aincraft.container.context;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.effects.IGemEffect;
import org.bukkit.event.Event;

final class Dispatch implements IDispatch {

  private final EffectQueuePool poolMap = new EffectQueuePool();

  public <T, C, E extends Event> void dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle) {
    ITriggerType<T> triggerType = context.triggerType();
    C eventContext = context.contextProvider().create(handle);
    EffectQueue queue = this.poolMap.acquire(triggerType, loader);
    if (!queue.isEmpty()) {
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
  }
}
