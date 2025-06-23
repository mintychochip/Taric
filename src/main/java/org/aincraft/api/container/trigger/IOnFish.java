package org.aincraft.api.container.trigger;

import org.aincraft.api.container.receiver.IExperienceContext;
import org.aincraft.api.container.receiver.IDropContext;
import org.aincraft.api.container.receiver.ITriggerContext;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface IOnFish {

  void onFish(IFishContext context);

  interface IFishContext extends ITriggerContext, IExperienceContext, IDropContext<ItemStack> {

    Player getPlayer();

    FishHook getHook();

    EquipmentSlot getHand();
  }
}
