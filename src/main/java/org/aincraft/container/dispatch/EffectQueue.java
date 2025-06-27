package org.aincraft.container.dispatch;

import com.google.common.collect.ForwardingQueue;
import java.util.PriorityQueue;
import java.util.Queue;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.container.registerable.ITriggerType;

final class EffectQueue extends ForwardingQueue<IEffectInstance> {

  private final PriorityQueue<IEffectInstance> queue;

  private final ITriggerType triggerType;

  public EffectQueue(PriorityQueue<IEffectInstance> queue, ITriggerType triggerType) {
    this.queue = queue;
    this.triggerType = triggerType;
  }

  @Override
  protected Queue<IEffectInstance> delegate() {
    return queue;
  }

  public ITriggerType getTriggerType() {
    return triggerType;
  }
}
