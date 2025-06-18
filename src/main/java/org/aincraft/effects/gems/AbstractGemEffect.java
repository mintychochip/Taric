package org.aincraft.effects.gems;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.aincraft.container.Rarity;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.util.Roman;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract class AbstractGemEffect implements IGemEffect {

  private static TriggerType[] TRIGGER_TYPES;

  private final String key;

  private GemEffectMeta meta;

  private Map<TriggerType, Set<Material>> builtTargets = null;

  AbstractGemEffect(String key) {
    this.key = key;
  }

  protected abstract Map<TriggerType, Set<Material>> buildValidTargets();


  private Map<TriggerType, Set<Material>> getValidTargets() {
    if (builtTargets == null) {
      builtTargets = buildValidTargets();
    }
    return builtTargets;
  }

  public void setMeta(GemEffectMeta meta) {
    this.meta = meta;
  }

  public GemEffectMeta getMeta() {
    return meta;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public int getMaxLevel() {
    return meta.maxLevel();
  }

  @Override
  public Component getDescription() {
    return meta.description();
  }

  @Override
  public int getPriority(TriggerType triggerType) {
    return meta.priority.getOrDefault(triggerType, 0);
  }

  @Override
  public boolean isValidTarget(TriggerType triggerType, ItemStack itemStack) {
    Map<TriggerType, Set<Material>> targets = this.getValidTargets();
    if (!targets.containsKey(triggerType)) {
      return false;
    }
    Material material = itemStack.getType();
    if (material.isAir()) {
      return false;
    }
    return targets.get(triggerType).contains(material);
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
  public Component getLabel(int rank) {
    Preconditions.checkArgument(rank > 0);
    return Component.empty()
        .append(Component.text(this.getName()))
        .append(Component.space())
        .append(Component.text(
            Roman.fromInteger(rank)));
  }

  @Override
  public String getName() {
    Class<? extends AbstractGemEffect> clazz = this.getClass();
    return clazz.getSimpleName().replaceAll("(?<!^)([A-Z])", " $1");
  }

  @Override
  public Rarity getRarity() {
    return meta.rarity;
  }

  @NotNull
  @Override
  public Set<EquipmentSlot> getRequiredActiveSlots() {
    return meta.requiredActiveSlots();
  }

  public record GemEffectMeta(int maxLevel,
                              Rarity rarity,
                              Map<TriggerType, Integer> priority,
                              Component description,
                              Set<EquipmentSlot> requiredActiveSlots) {

  }
}
