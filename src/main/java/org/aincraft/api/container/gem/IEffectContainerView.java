package org.aincraft.api.container.gem;

import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public interface IEffectContainerView extends Iterable<Entry<Key, Integer>> {

  NamespacedKey getKey();

  int getRank(IGemEffect effect);

  boolean has(IGemEffect effect);

  void update(ItemStack stack);
}
