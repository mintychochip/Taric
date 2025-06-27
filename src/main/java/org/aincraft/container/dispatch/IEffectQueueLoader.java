package org.aincraft.container.dispatch;

import java.util.Queue;
import org.aincraft.api.container.IEffectInstance;

public interface IEffectQueueLoader {

  void load(Queue<IEffectInstance> queue);
}
