package org.aincraft.container;

import com.google.common.base.Preconditions;
import org.aincraft.api.container.IEquipment;
import org.aincraft.api.container.IEquipment.IEquipmentFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class EquipmentFactory implements IEquipmentFactory {

  @Override
  public IEquipment create(Entity entity) throws IllegalArgumentException {
    Preconditions.checkArgument(entity instanceof LivingEntity);
    if (entity instanceof Player player) {
      return new PlayerEquipment(player.getInventory());
    }
    return new EntityEquipment(((LivingEntity) entity).getEquipment());
  }
}
