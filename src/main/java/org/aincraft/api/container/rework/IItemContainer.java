package org.aincraft.api.container.rework;

import org.aincraft.effects.IGemEffect;

public interface IItemContainer {
  void setCount(SocketType type, int count);
  int getCount(SocketType type);
}
