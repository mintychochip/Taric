package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.receiver.IExperienceContext;
import org.aincraft.api.container.receiver.IReceiveDrops;
import org.aincraft.api.container.receiver.ITriggerContext;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IOnKillEntity {

  void onKillEntity(IKillEntityContext context);

  interface IKillEntityContext extends ITriggerContext, IReceiveDrops<List<ItemStack>>,
      IExperienceContext {

    DamageSource getDamageSource();

    LivingEntity getSlain();
  }
}
