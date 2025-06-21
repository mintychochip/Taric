package org.aincraft.container.trigger;

import java.util.List;
import org.aincraft.api.container.trigger.IOnKillEntity.IKillEntityContext;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class KillTriggerContext extends AbstractTriggerContext<EntityDeathEvent> implements
    IKillEntityContext {

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
  public @NotNull List<ItemStack> getDrops() {
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
