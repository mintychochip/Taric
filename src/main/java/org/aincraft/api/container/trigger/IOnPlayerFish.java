package org.aincraft.api.container.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.context.IDropContext;
import org.aincraft.api.container.context.IExperienceContext;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface IOnPlayerFish {

  interface IPlayerFishContext extends IExperienceContext, IDropContext<ItemStack> {

    Player getPlayer();

    FishHook getHook();

    EquipmentSlot getHand();
  }

  void onPlayerFish(IPlayerFishContext context, EffectInstanceMeta meta);
}
