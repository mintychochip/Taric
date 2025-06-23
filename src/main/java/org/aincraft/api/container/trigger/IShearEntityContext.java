package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.receiver.IDropContext;
import org.aincraft.api.container.receiver.ITriggerContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IShearEntityContext extends ITriggerContext, IDropContext<List<ItemStack>> {

  @NotNull
  ItemStack getTool();

  @NotNull
  Entity getSheared();

  interface IPlayerShearContext extends IShearEntityContext {

    @NotNull
    Player getPlayer();
  }
}
