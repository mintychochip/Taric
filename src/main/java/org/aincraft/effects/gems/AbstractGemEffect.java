package org.aincraft.effects.gems;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

abstract class AbstractGemEffect implements IGemEffect {

  private static TriggerType[] TRIGGER_TYPES;

  private GemEffectMeta meta;

  private final Supplier<Map<TriggerType, Set<Material>>> targetSupplier = Suppliers.memoize(
      this::buildValidTargets);

  private final Supplier<NamespacedKey> keySupplier = Suppliers.memoize(this::buildKey);

  private final Supplier<String> nameSupplier = Suppliers.memoize(this::buildName);

  protected String buildName() {
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    return clazz.getSimpleName().replaceAll("(?<!^)([A-Z])", " $1");
  }

  protected NamespacedKey buildKey() {
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    String key = clazz.getSimpleName().replaceAll("(?<!^)([A-Z])", "-$1");
    return new NamespacedKey("taric", key.toLowerCase());
  }

  protected abstract Map<TriggerType, Set<Material>> buildValidTargets();

  private Map<TriggerType, Set<Material>> getValidTargets() {
    return targetSupplier.get();
  }

  public void setMeta(GemEffectMeta meta) {
    this.meta = meta;
  }

  public GemEffectMeta getMeta() {
    return meta;
  }

  @Override
  public @NotNull NamespacedKey getKey() {
    return keySupplier.get();
  }

  @Override
  public int getMaxRank() {
    return meta.maxLevel();
  }

  @Override
  public String getDescription() {
    return meta.description();
  }

  @Override
  public int getPriority(TriggerType triggerType) {
    return meta.priority.getOrDefault(triggerType, 0);
  }

  @Override
  public boolean isValidTarget(TriggerType triggerType, Material material) {
    if (material.isAir()) {
      return false;
    }
    Map<TriggerType, Set<Material>> validTargets = this.getValidTargets();
    if (!validTargets.containsKey(triggerType)) {
      return false;
    }
    return validTargets.get(triggerType).contains(material);
  }

  @Override
  public boolean isValidTarget(Material material) {
    if (material.isAir()) {
      return false;
    }
    Map<TriggerType, Set<Material>> validTargets = this.getValidTargets();
    for (Set<Material> materials : validTargets.values()) {
      if (materials.contains(material)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public TriggerType[] getTriggerTypes() {
    if (TRIGGER_TYPES != null) {
      return TRIGGER_TYPES;
    }
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    Class<?>[] interfaces = clazz.getInterfaces();
    TRIGGER_TYPES = new TriggerType[interfaces.length];
    int count = 0;
    for (Class<?> iface : interfaces) {
      TriggerType triggerType = TriggerType.find(iface);
      if (triggerType == null) {
        continue;
      }
      TRIGGER_TYPES[count++] = triggerType;
    }

    return TRIGGER_TYPES;
  }

  @Override
  public String getName() {
    return nameSupplier.get();
  }

  @Override
  public IRarity getRarity() {
    return meta.rarity;
  }

  @Override
  public ISocketColor getColor() {
    return meta.color;
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

  public record GemEffectMeta(int maxLevel,
                              IRarity rarity,
                              ISocketColor color,
                              Map<TriggerType, Integer> priority,
                              String description,
                              Set<EquipmentSlot> requiredActiveSlots) {

  }
}
