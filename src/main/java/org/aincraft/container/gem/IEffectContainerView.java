package org.aincraft.container.gem;

import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerView {

  NamespacedKey getKey();

  int getRank(IGemEffect effect);

  boolean has(IGemEffect effect);

  void update(ItemStack stack);
}
