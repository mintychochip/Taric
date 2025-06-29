package org.aincraft.effects;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Taric;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

abstract class AbstractGemEffect implements IGemEffect {

  private final Supplier<Map<ITriggerType<?>, Set<Material>>> targetSupplier = Suppliers.memoize(
      this::buildValidTargets);
  private final Supplier<NamespacedKey> keySupplier = Suppliers.memoize(this::buildKey);
  private final Supplier<String> nameSupplier = Suppliers.memoize(this::buildName);
  private GemEffectMeta meta;

  public record GemEffectMeta(int maxLevel,
                              IRarity rarity,
                              ISocketColor color,
                              List<String> adjectives,
                              Map<ITriggerType<?>, Integer> priority,
                              String description,
                              Set<EquipmentSlot> requiredActiveSlots) {

  }

  @Override
  public int getPriority(ITriggerType<?> triggerType) {
    return meta.priority.getOrDefault(triggerType, 0);
  }

  @Override
  public IRarity getRarity() {
    return meta.rarity;
  }

  @Override
  public ISocketColor getSocketColor() {
    return meta.color;
  }


  @Override
  public boolean isValidTarget(ITriggerType<?> trigger, Material material) {
    if (material.isAir()) {
      return false;
    }
    Map<ITriggerType<?>, Set<Material>> validTargets = this.getValidTargets();
    if (!validTargets.containsKey(trigger)) {
      return false;
    }
    return validTargets.get(trigger).contains(material);
  }

  private Map<ITriggerType<?>, Set<Material>> getValidTargets() {
    return this.targetSupplier.get();
  }

  @Override
  public boolean isValidTarget(Material material) {
    if (material.isAir()) {
      return false;
    }
    Map<ITriggerType<?>, Set<Material>> validTargets = this.getValidTargets();
    for (Set<Material> materials : validTargets.values()) {
      if (materials.contains(material)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getAdjective() {
    int size = meta.adjectives.size();
    if (size == 0) {
      return "";
    }
    if (size == 1) {
      return meta.adjectives.getFirst();
    }
    int index = Taric.getRandom().nextInt(size);
    return meta.adjectives.get(index);
  }

  @Override
  public int getMaxRank() {
    return meta.maxLevel();
  }

  @Override
  public Permission getPermission() {
    return null;
  }

  @NotNull
  @Override
  public Set<EquipmentSlot> getRequiredActiveSlots() {
    return meta.requiredActiveSlots();
  }

  @Override
  public boolean isValidSlot(EquipmentSlot slot) {
    return meta.requiredActiveSlots.contains(slot);
  }

  @Override
  public String getDescription() {
    return meta.description();
  }

  @Override
  public String getName() {
    return nameSupplier.get();
  }

  @Override
  public @NotNull NamespacedKey getKey() {
    return keySupplier.get();
  }

  @Override
  public double getWeight() {
    return meta.rarity.getWeight();
  }

  public void setMeta(GemEffectMeta meta) {
    this.meta = meta;
  }

  protected String buildName() {
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    return clazz.getSimpleName().replaceAll("(?<!^)([A-Z])", " $1");
  }

  protected NamespacedKey buildKey() {
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    String key = clazz.getSimpleName().replaceAll("(?<!^)([A-Z])", "-$1");
    return new NamespacedKey("taric", key.toLowerCase());
  }

  protected abstract Map<ITriggerType<?>, Set<Material>> buildValidTargets();
}
