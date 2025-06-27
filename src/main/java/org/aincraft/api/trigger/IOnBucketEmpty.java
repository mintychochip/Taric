package org.aincraft.api.trigger;

import org.aincraft.api.container.Mutable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IOnBucketEmpty {

  void onBucketEmpty(int rank, Player player, ItemStack bucket, Material bucketMaterial,
      EquipmentSlot hand,
      @Nullable Mutable<ItemStack> stackAfter);
}
