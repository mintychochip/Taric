package org.aincraft.container.context;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import org.aincraft.api.trigger.ITriggerType;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class EffectQueuePool {

  private final Map<ITriggerType<?>, Queue<EffectQueue>> poolMap;

  EffectQueuePool() {
    poolMap = new ConcurrentHashMap<>();
  }

  public synchronized EffectQueue acquire(ITriggerType<?> trigger, IEffectQueueLoader loader)
      throws NullPointerException {
    Preconditions.checkNotNull(trigger);
    Queue<EffectQueue> pool = poolMap.computeIfAbsent(trigger,
        key -> new ArrayDeque<>());
    EffectQueue queue = pool.poll();
    if (queue == null) {
      queue = new EffectQueue(
          new PriorityQueue<>(Comparator.comparingInt(a -> a.getEffect().getPriority(trigger))),
          trigger);
    } else {
      queue.clear();
    }
    loader.load(queue);
    return queue;
  }

  public synchronized void release(EffectQueue queue) {
    ITriggerType<?> trigger = queue.getTriggerType();
    Queue<EffectQueue> pool = poolMap.get(trigger);
    pool.offer(queue);
  }
}
