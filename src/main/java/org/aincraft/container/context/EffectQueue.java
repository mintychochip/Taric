package org.aincraft.container.context;

import com.google.common.collect.ForwardingQueue;
import java.util.PriorityQueue;
import java.util.Queue;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.api.trigger.TriggerType;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class EffectQueue extends ForwardingQueue<IEffectInstance> {

  private final PriorityQueue<IEffectInstance> queue;

  private final TriggerType<?> triggerType;

  EffectQueue(PriorityQueue<IEffectInstance> queue, TriggerType<?> triggerType) {
    this.queue = queue;
    this.triggerType = triggerType;
  }

  @Override
  protected Queue<IEffectInstance> delegate() {
    return queue;
  }

  public TriggerType<?> getTriggerType() {
    return triggerType;
  }
}
