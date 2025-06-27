package org.aincraft.container.dispatch;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.effects.IGemEffect;
import org.bukkit.event.Event;

final class Dispatch implements IDispatch {

  private final RewrittenEffectQueue poolMap = new RewrittenEffectQueue();

  public <T, C, E extends Event> void dispatch(IDispatchContext<T, C, E> context,
      IEffectQueueLoader loader, E handle) {
    ITriggerType<T> triggerType = context.triggerType();
    C eventContext = context.contextProvider().create(handle);
    EffectQueue queue = this.poolMap.acquire(triggerType, loader);
    if (!queue.isEmpty()) {
      for (IEffectInstance instance : queue) {
        EffectInstanceMeta meta = instance.getMeta();
        IGemEffect rawEffect = instance.getEffect();
        T cast = triggerType.getTriggerClazz().cast(rawEffect);
        context.trigger(cast, eventContext, meta);
      }
    }
    this.poolMap.release(queue);
  }
}
