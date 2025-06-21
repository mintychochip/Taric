package org.aincraft.container.rework;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import org.aincraft.api.container.rework.IItemContainer;
import org.aincraft.api.container.rework.SocketType;
import org.aincraft.effects.IGemEffect;

public class ItemContainer implements IItemContainer {

  private final Map<SocketType, Integer> socketCount = new HashMap<>();
  private final Queue<>
  @Override

  public boolean hasEffect(IGemEffect effect) {
    return false;
  }

  @Override
  public void setCount(SocketType type, int count) {
    socketCount.put(type, count);
  }

  @Override
  public int getCount(SocketType type) {
    return socketCount.get(type);
  }
}
