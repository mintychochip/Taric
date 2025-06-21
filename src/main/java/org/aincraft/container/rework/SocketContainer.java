package org.aincraft.container.rework;

import it.unimi.dsi.fastutil.Hash;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.rework.ISocketContainer;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.IGemEffect;

public class SocketContainer implements ISocketContainer {

  private final Map<ISocketColor, Integer> socketCount = new HashMap<>();
  private final Queue<EffectInstance> queue = new Hash
  @Override
  public void setCount(ISocketColor color, int count) {
    socketCount.put(color, count);
  }

  @Override
  public void addCount(ISocketColor color) {
    int count = this.getCount(color);
    this.setCount(color, count + 1);
  }

  @Override
  public int getCount(ISocketColor color) {
    Integer count = socketCount.get(color);
    return count != null ? count : 0;
  }

  @Override
  public String toString() {
    return socketCount.toString();
  }

  @Override
  public void addEffect(IGemEffect effect, int rank) {
    ISocketColor color = effect.getColor();
    int count = this.getCount(color);

  }

  static final class Socket
}
