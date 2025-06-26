package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

final class Crush extends AbstractGemEffect implements IOnBlockDrop {

  private final CrushHelperBlockDrop helper = new CrushHelperBlockDrop();

  static final class CrushHelperBlockDrop extends BlockDropConversionHelper {

    @Override
    protected boolean conversionPredicate(IBlockDropContext context, int rank, ItemStack stack) {
      Material material = stack.getType();
      return material == Material.COBBLESTONE || material == Material.GRAVEL;
    }

    @Override
    protected ItemStack conversion(Material material) {
      return new ItemStack(material == Material.COBBLESTONE ? Material.GRAVEL : Material.SAND);
    }
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_DROP,
            TypeSet.builder().union(TargetType.PICKAXE, TargetType.SHOVEL).build())
    );
  }

  @Override
  public void onBlockDrop(IBlockDropContext context, int rank) {
    helper.onBlockDrop(context, rank);
  }
}
