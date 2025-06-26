package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.context.IDropContext;
import org.aincraft.api.container.context.IExperienceContext;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IOnKillEntity {

  interface IKillEntityContext extends IDropContext<List<ItemStack>>,
      IExperienceContext {

    DamageSource getDamageSource();

    LivingEntity getSlain();
  }

  void onKillEntity(IKillEntityContext context, int rank);
}
