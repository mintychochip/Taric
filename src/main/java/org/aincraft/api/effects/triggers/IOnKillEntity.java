package org.aincraft.api.effects.triggers;

import java.util.List;
import org.aincraft.api.container.Mutable;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IOnKillEntity {

  void onKillEntity(int rank, DamageSource damageSource, LivingEntity entity,
      Mutable<Integer> experience, List<ItemStack> drops);
}
