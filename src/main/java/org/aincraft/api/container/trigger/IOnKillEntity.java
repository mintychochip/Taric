package org.aincraft.api.container.trigger;

import org.aincraft.api.container.receiver.IReceiveDrops;
import org.aincraft.api.container.receiver.IReceiveExperience;
import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;

public interface IOnKillEntity {

  void onKillEntity(IKillEntityReceiver receiver);

  interface IKillEntityReceiver extends ITriggerReceiver, IReceiveDrops,
      IReceiveExperience {

    DamageSource getDamageSource();

    LivingEntity getSlain();
  }
}
