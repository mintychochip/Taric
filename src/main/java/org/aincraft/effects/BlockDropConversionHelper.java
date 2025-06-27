package org.aincraft.effects;

import java.util.List;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

abstract class BlockDropConversionHelper implements IOnBlockDrop {

  @Override
  public void onBlockDrop(IBlockDropContext context, EffectInstanceMeta meta) {
    List<ItemStack> drops = context.getDrops();
    if (drops == null || drops.isEmpty()) {
      return;
    }
    for (int i = 0; i < drops.size(); ++i) {
      ItemStack drop = drops.get(i);
      Material material = drop.getType();
      if (material.isItem() && conversionPredicate(context, meta, drop)) {
        ItemStack stack = conversion(material);
        if (stack == null) {
          continue;
        }
        stack.setAmount(drop.getAmount());
        drops.set(i, stack);
      }
    }
    context.setDrops(drops);
  }

  protected abstract boolean conversionPredicate(IBlockDropContext context, EffectInstanceMeta meta,
      ItemStack stack);

  protected abstract ItemStack conversion(Material material);
}
