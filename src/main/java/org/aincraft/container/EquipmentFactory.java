package org.aincraft.container;

import com.google.common.base.Preconditions;
import org.aincraft.api.container.Equipment;
import org.aincraft.api.context.ContextFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class EquipmentFactory {

  public static Equipment create(Entity entity) throws IllegalArgumentException {
    Preconditions.checkArgument(entity instanceof LivingEntity);
    if (entity instanceof Player player) {
      return new ContextFactory.ContextBinder<>(player.getInventory()).build(Equipment.class);
    }
    return new ContextFactory.ContextBinder<>(((LivingEntity) entity).getEquipment()).build(
        Equipment.class);
  }
}
