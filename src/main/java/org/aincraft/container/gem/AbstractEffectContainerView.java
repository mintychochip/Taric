package org.aincraft.container.gem;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

abstract class AbstractEffectContainerView<CImpl extends AbstractEffectContainer<C, V>, C extends IEffectContainer<C, V>, V extends IEffectContainerView> implements
    IEffectContainerView {

  protected final Map<Key, Integer> effects;
  protected final NamespacedKey key;
  protected final CImpl container;

  AbstractEffectContainerView(Map<Key, Integer> effects, NamespacedKey key,
      CImpl container) {
    this.effects = effects;
    this.key = key;
    this.container = container;
  }

  @Override
  public int getRank(IGemEffect effect) {
    return effects.get(effect.key());
  }

  @Override
  public boolean has(IGemEffect effect) {
    return effects.containsKey(effect.key());
  }

  @Override
  public void update(ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return;
    }
    stack.setData(DataComponentTypes.LORE, effectsToLore());
    stack.editPersistentDataContainer(pdc -> {
      String json = Taric.getGson().toJson(container);
      pdc.set(this.getKey(),
          PersistentDataType.STRING, json);
    });
  }

  protected ItemLore effectsToLore() {
    ItemLore.Builder builder = ItemLore.lore();
    for (Entry<Key, Integer> entry : effects.entrySet()) {
      IGemEffect effect = Taric.getEffects().get(entry.getKey());
      if (effect == null) {
        continue;
      }
      Component label = Component.empty().append(Component.text(effect.getName()))
          .append(Component.space()).append(Component.text(
              Roman.fromInteger(entry.getValue()))).color(NamedTextColor.BLUE)
          .decoration(TextDecoration.ITALIC, false);
      builder.addLine(label);
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

  @NotNull
  @Override
  public Iterator<Entry<Key, Integer>> iterator() {
    return new LinkedHashMap<>(effects).entrySet().iterator();
  }
}
