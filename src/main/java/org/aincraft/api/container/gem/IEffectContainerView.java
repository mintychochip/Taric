package org.aincraft.api.container.gem;

import org.aincraft.effects.IGemEffect;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerView {

  int getRank(IGemEffect effect);

  void update(ItemStack stack);
}
