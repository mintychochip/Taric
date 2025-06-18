package org.aincraft.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import org.aincraft.Taric;
import org.aincraft.container.GemItem.GemItemContents;
import org.aincraft.container.IQueueLoader.IQueueLoaderHolder;
import org.aincraft.container.equipment.EntityEquipment;
import org.aincraft.container.equipment.IEquipment;
import org.aincraft.container.equipment.PlayerEquipment;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GemInventory implements IQueueLoaderHolder<EffectInstance> {

  private final Map<EquipmentSlot, GemItem> inventory;
  private final Map<TriggerType, IQueueLoader<EffectInstance>> loaders = new HashMap<>();

  public GemInventory(Map<EquipmentSlot, GemItem> inventory) {
    this.inventory = inventory;
  }

  @Nullable
  public GemItem getItem(EquipmentSlot slot) {
    return inventory.get(slot);
  }


  @NotNull
  public static GemInventory from(@NotNull Entity entity) {
    Map<EquipmentSlot, GemItem> inventory = new HashMap<>();
    IEquipment equipment;
    if (entity instanceof Player player) {
      equipment = new PlayerEquipment(player.getInventory());
    } else if (entity instanceof LivingEntity livingEntity) {
      equipment = new EntityEquipment(livingEntity.getEquipment());
    } else {
      throw new IllegalStateException(
          "the entity must have equipment, check if the entity is living or a player first.");
    }
    for (EquipmentSlot slot : EquipmentSlot.values()) {
      try {
        ItemStack stack = equipment.getItem(slot);
        GemItem gemItem = GemItem.from(stack);
        if (gemItem == null) {
          continue;
        }
        inventory.put(slot, gemItem);
      } catch (IllegalArgumentException ignored) {
      }
    }
    return new GemInventory(inventory);
  }

  @Override
  public String toString() {
    return Taric.getGson().toJson(inventory);
  }

  @Override
  public IQueueLoader<EffectInstance> getLoader(TriggerType triggerType) {
    return loaders.computeIfAbsent(triggerType, t -> queue -> fillQueue(t, queue));
  }

  public void fillQueue(TriggerType triggerType, Queue<EffectInstance> queue) {
    for (Map.Entry<EquipmentSlot, GemItem> itemEntry : inventory.entrySet()) {
      GemItem gemItem = itemEntry.getValue();
      GemItemContents gemItemContents = gemItem.contents();
      for (Map.Entry<String, Integer> effectEntry : gemItemContents.getEffects().entrySet()) {
        String effectId = effectEntry.getKey();
        int priority = effectEntry.getValue();
        IGemEffect effect = Taric.getEffects().get(effectId);
        if (triggerType.hasTriggerType(effect) && effect.isValidSlot(itemEntry.getKey())
            && effect.isValidTarget(triggerType, gemItem.item())) {
          queue.add(new EffectInstance(effect, priority));
        }
      }
    }
  }
}

