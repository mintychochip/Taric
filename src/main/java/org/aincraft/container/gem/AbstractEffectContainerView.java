package org.aincraft.container.gem;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

abstract class AbstractEffectContainerView implements IEffectContainerView {

  protected final Map<String, Integer> effects;
  protected final NamespacedKey key;

  AbstractEffectContainerView(Map<String, Integer> effects, NamespacedKey key) {
    this.effects = effects;
    this.key = key;
  }

  @Override
  public int getRank(IGemEffect effect) {
    return effects.get(effect.getKey());
  }

  @Override
  public boolean has(IGemEffect effect) {
    return effects.containsKey(effect.getKey());
  }

  @Override
  public void update(ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return;
    }
    stack.setData(DataComponentTypes.LORE, effectsToLore());
    stack.editPersistentDataContainer(pdc -> {
      pdc.set(this.getKey(),
          PersistentDataType.STRING, Taric.getGson().toJson(this));
    });
  }

  protected ItemLore effectsToLore() {
    ItemLore.Builder builder = ItemLore.lore();
    for (Entry<String, Integer> entry : effects.entrySet()) {
      IGemEffect effect = Taric.getEffects().get(entry.getKey());
      if (effect == null) {
        continue;
      }
      builder.addLine(effect.getLabel(entry.getValue()).color(NamedTextColor.BLUE).decoration(
          TextDecoration.ITALIC, false));
    }
    return builder.build();
  }

  @Override
  public NamespacedKey getKey() {
    return key;
  }

  @Override
  public String toString() {
    return effects.toString();
  }
}
