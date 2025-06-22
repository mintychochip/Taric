package org.aincraft.api.container.rework;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.effects.IGemEffect;

public interface ISocketContainer {

  void setCount(ISocketColor color, int count);

  void addCount(ISocketColor color);

  int getCount(ISocketColor color);

  void addEffect(IGemEffect effect, int rank, boolean force);
}
