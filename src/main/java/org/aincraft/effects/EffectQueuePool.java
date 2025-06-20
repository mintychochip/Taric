package org.aincraft.effects;

import com.google.common.collect.ForwardingQueue;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.container.IQueueLoader;
import org.aincraft.container.IQueueLoader.IQueueLoaderHolder;
import org.aincraft.effects.EffectQueuePool.EffectInstance;

public final class EffectQueuePool<T extends EffectInstance> {

  private final Map<TriggerType, Queue<EffectQueue<T>>> pool = new ConcurrentHashMap<>();

  public synchronized EffectQueue<T> acquireQueue(TriggerType triggerType) {
    Queue<EffectQueue<T>> queuePool = pool.computeIfAbsent(triggerType, key -> new ArrayDeque<>());
    EffectQueue<T> queue = queuePool.poll();
    if (queue == null) {
      queue = new EffectQueue<>(triggerType,new PriorityQueue<>(greater_than(triggerType)));
    } else {
      queue.clear();
    }
    return queue;
  }

  public synchronized EffectQueue<T> acquireAndFill(TriggerType triggerType, IQueueLoader<T> loader) {
    EffectQueue<T> queue = this.acquireQueue(triggerType);
    loader.load(queue);
    return queue;
  }

  public EffectQueue<T> acquireAndFill(TriggerType triggerType, IQueueLoaderHolder<T> holder) {
    return this.acquireAndFill(triggerType,holder.getLoader(triggerType));
  }

  public synchronized void release(EffectQueue<T> queue) {
    pool.computeIfAbsent(queue.getTriggerType(), key -> new ArrayDeque<>()).offer(queue);
  }

  private static <T extends EffectInstance> Comparator<T> greater_than(TriggerType type) {
    return Comparator.comparingInt(a -> a.getEffect().getPriority(type));
  }

  public static final class EffectQueue<T extends EffectInstance> extends
      ForwardingQueue<T> implements Iterable<T> {

    private final PriorityQueue<T> queue;
    private final TriggerType triggerType;

    EffectQueue(TriggerType triggerType, PriorityQueue<T> queue) {
      this.triggerType = triggerType;
      this.queue = queue;
    }

    public TriggerType getTriggerType() {
      return triggerType;
    }

    public PriorityQueue<T> getQueue() {
      return queue;
    }

    public boolean isEmpty() {
      return queue.isEmpty();
    }

    @Override
    protected Queue<T> delegate() {
      return queue;
    }

    public void clear() {
      queue.clear();
    }
  }


  public static class EffectInstance {

    private final IGemEffect effect;
    private final int rank;

    public EffectInstance(IGemEffect effect, int rank) {
      this.effect = effect;
      this.rank = rank;
    }

    public IGemEffect getEffect() {
      return effect;
    }

    public int getRank() {
      return rank;
    }
  }
}
