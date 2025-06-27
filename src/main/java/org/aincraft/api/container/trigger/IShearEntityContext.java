package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.context.IDropContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IShearEntityContext extends IDropContext<List<ItemStack>> {

  interface IPlayerShearEntityContext extends IShearEntityContext {

    @NotNull
    Player getPlayer();
  }

  @NotNull
  ItemStack getTool();

  @NotNull
  Entity getSheared();
}
