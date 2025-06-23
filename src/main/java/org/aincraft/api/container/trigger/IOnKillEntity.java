package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.context.IDropContext;
import org.aincraft.api.container.context.IExperienceContext;
import org.aincraft.api.container.context.ITriggerContext;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IOnKillEntity {

  void onKillEntity(IKillEntityContext context);

  interface IKillEntityContext extends ITriggerContext, IDropContext<List<ItemStack>>,
      IExperienceContext {

    DamageSource getDamageSource();

    LivingEntity getSlain();
  }
}
