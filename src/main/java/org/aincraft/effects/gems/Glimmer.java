package org.aincraft.effects.gems;

import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnFish;
import org.aincraft.api.container.trigger.IOnSocket;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

final class Glimmer extends AbstractGemEffect implements IOnFish, IOnSocket {

  @Override
  public void onFish(IFishContext context) {
    context.getHook().setGlowing(true);
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.FISH, TypeSet.single(Material.FISHING_ROD))
    );
  }

  @Override
  public void onSocket(ItemStack stack) {
    Map<Enchantment, Integer> enchantments = stack.getEnchantments();
    if (!enchantments.isEmpty()) {
      return;
    }
    stack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
  }

  @Override
  public void onUnSocket(ItemStack stack) {
    Map<Enchantment, Integer> enchantments = stack.getEnchantments();
    if (!enchantments.isEmpty()) {
      return;
    }
    stack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
  }
}
