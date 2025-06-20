package org.aincraft.container;

import java.util.Queue;
import org.aincraft.api.container.trigger.TriggerType;

public interface IQueueLoader<T> {
  void load(Queue<T> queue);

  interface IQueueLoaderHolder<T> {
    IQueueLoader<T> getLoader(TriggerType triggerType);
  }
}
