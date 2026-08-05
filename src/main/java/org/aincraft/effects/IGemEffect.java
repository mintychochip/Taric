package org.aincraft.effects;

import java.util.Set;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.IWeighable;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

public interface IGemEffect extends Keyed, IWeighable {

  int getPriority(ITriggerType<?> triggerType);

  IRarity getRarity();

  ISocketColor getSocketColor();

  boolean isValidTarget(ITriggerType<?> trigger, Material material);

  boolean isValidTarget(Material material);

  String getAdjective();

  int getMaxRank();

  Permission getPermission();

  /**
   * Returns the set of equipment slots from which the effect can be activated. If the item
   * containing the effect is not in one of these slots, the effect will not be triggered.
   */
  @NotNull
  Set<EquipmentSlot> getRequiredActiveSlots();

  boolean isValidSlot(EquipmentSlot slot);

  String getDescription();

  String getName();
}
