package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.Mutable;
import org.aincraft.api.trigger.IOnBucketEmpty;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Overflowing extends AbstractGemEffect implements IOnBucketEmpty {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
//        Map.entry(TriggerTypes.BUCKET_EMPTY, TypeSet.single(Material.WATER_BUCKET))
    );
  }

  @Override
  public void onBucketEmpty(int rank, Player player, ItemStack bucket, Material bucketMaterial,
      EquipmentSlot hand, @Nullable Mutable<ItemStack> stackAfter) {

  }
}
