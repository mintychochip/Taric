package org.aincraft.container.trigger;

import java.util.List;
import org.aincraft.api.container.trigger.IOnKillEntity.IKillEntityReceiver;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public final class KillTriggerReceiver extends AbstractTriggerReceiver<EntityDeathEvent> implements
    IKillEntityReceiver {

  @Override
  public DamageSource getDamageSource() {
    return event.getDamageSource();
  }

  @Override
  public LivingEntity getSlain() {
    return event.getEntity();
  }

  @Override
  public void setDrops(List<ItemStack> drops) {
    event.getDrops().clear();
    event.getDrops().addAll(drops);
  }

  @Override
  public List<ItemStack> getDrops() {
    return event.getDrops();
  }

  @Override
  public void setExperience(int exp) {
    event.setDroppedExp(exp);
  }

  @Override
  public int getExperience() {
    return event.getDroppedExp();
  }
}
