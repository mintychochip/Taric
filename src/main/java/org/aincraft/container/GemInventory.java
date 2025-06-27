package org.aincraft.container;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.container.IEquipment;
import org.aincraft.api.container.IEquipment.IEquipmentFactory;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.gem.IGemInventoryFactory;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class GemInventory implements IGemInventory {

  private final IEquipment equipment;
  private final Map<EquipmentSlot, IGemItem> inventory;

  GemInventory(IEquipment equipment, Map<EquipmentSlot, IGemItem> inventory) {
    this.equipment = equipment;
    this.inventory = inventory;
  }

  static final class GemInventoryFactory implements IGemInventoryFactory {

    private final IEquipmentFactory equipmentFactory;
    private final IGemItemFactory gemItemFactory;

    @Inject
    GemInventoryFactory(IEquipmentFactory equipmentFactory, IGemItemFactory gemItemFactory) {
      this.equipmentFactory = equipmentFactory;
      this.gemItemFactory = gemItemFactory;
    }

    @Override
    public IGemInventory create(@NotNull LivingEntity entity) {
      Map<EquipmentSlot, IGemItem> inventory = new HashMap<>();
      IEquipment equipment = equipmentFactory.create(entity);
      for (EquipmentSlot slot : EquipmentSlot.values()) {
        try {
          ItemStack stack = equipment.getItem(slot);
          IGemItem gemItem = gemItemFactory.fromIfExists(stack);
          if (gemItem == null) {
            continue;
          }
          inventory.put(slot, gemItem);
        } catch (IllegalArgumentException ignored) {

        }
      }
      return new GemInventory(equipment, inventory);
    }
  }

  @Override
  public IEquipment getEquipment() {
    return equipment;
  }

  @Override
  public IEffectQueueLoader getLoader(ITriggerType<?> trigger) {
    return queue -> {
      for (Map.Entry<EquipmentSlot, IGemItem> itemEntry : inventory.entrySet()) {
        EquipmentSlot slot = itemEntry.getKey();
        IGemItem gemItem = itemEntry.getValue();
        IEffectQueueLoader loader = gemItem.getLoader(trigger, slot);
        loader.load(queue);
      }
    };
  }
}
