package org.aincraft.api.container.receiver;

import org.jetbrains.annotations.Nullable;

public interface IDropContext<D> {
  void setDrops(D drops);
  @Nullable
  D getDrops();
}
