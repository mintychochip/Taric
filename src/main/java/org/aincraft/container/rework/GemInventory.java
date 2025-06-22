package org.aincraft.container.rework;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import org.aincraft.api.container.IEquipment;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.container.IQueueLoader;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.IGemEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class GemInventory implements IGemInventory {

  private final IEquipment equipment;
  private final Map<EquipmentSlot, IGemItem> inventory;

  private final Map<TriggerType, IQueueLoader<EffectInstance>> loaders = new HashMap<>();

  GemInventory(IEquipment equipment, Map<EquipmentSlot, IGemItem> inventory) {
    this.equipment = equipment;
    this.inventory = inventory;
  }

  @NotNull
  public static IGemInventory from(@NotNull LivingEntity entity) {
    return GemItemFactory.inventoryFromEntity(entity);
  }

  @Override
  public IQueueLoader<EffectInstance> getLoader(TriggerType triggerType) {
    return loaders.computeIfAbsent(triggerType, t -> queue -> fillQueue(t, queue));
  }

  @Override
  public IEquipment getEquipment() {
    return equipment;
  }

  public void fillQueue(TriggerType triggerType, Queue<EffectInstance> queue) {
    for (Map.Entry<EquipmentSlot, IGemItem> itemEntry : inventory.entrySet()) {
      IGemItem gemItem = itemEntry.getValue();
      for (Entry<IGemEffect, Integer> entry : gemItem.getEffectContainer()) {
        int rank = entry.getValue();
        IGemEffect effect = entry.getKey();
        if (triggerType.hasTriggerType(effect) && effect.isValidSlot(itemEntry.getKey())
            && effect.isValidTarget(triggerType, gemItem.getStack().getType())) {
          queue.add(new EffectInstance(effect, rank));
        }
      }
    }
  }
}
