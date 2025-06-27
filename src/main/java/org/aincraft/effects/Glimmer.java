package org.aincraft.effects;

import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.trigger.IOnPlayerFish;
import org.aincraft.api.trigger.IOnSocket;
import org.aincraft.api.context.IPlayerFishContext;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

final class Glimmer extends AbstractGemEffect implements IOnPlayerFish, IOnSocket {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.PLAYER_FISH, TypeSet.single(Material.FISHING_ROD))
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

  @Override
  public void onPlayerFish(IPlayerFishContext context, EffectInstanceMeta meta) {
    context.getHook().setGlowing(true);
  }
}
