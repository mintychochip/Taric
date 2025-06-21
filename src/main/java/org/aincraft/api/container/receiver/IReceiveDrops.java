package org.aincraft.api.container.receiver;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IReceiveDrops<D> {
  void setDrops(D drops);
  @Nullable
  D getDrops();
}
