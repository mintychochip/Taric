package org.aincraft.container.equipment;

import org.aincraft.api.container.IEquipment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class EquipmentFactory {

  @NotNull
  public static IEquipment equipmentFromEntity(Entity entity) throws IllegalStateException {
    if (entity instanceof Player player) {
      return new PlayerEquipment(player.getInventory());
    } else if (entity instanceof LivingEntity livingEntity) {
      return new EntityEquipment(livingEntity.getEquipment());
    } else {
      throw new IllegalStateException(
          "the entity must have equipment, check if it is living first");
    }
  }
}
