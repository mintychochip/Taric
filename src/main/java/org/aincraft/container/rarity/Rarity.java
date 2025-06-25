package org.aincraft.container.rarity;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.util.random.WeightedRandom;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.util.IRandomSelector;
import org.jetbrains.annotations.NotNull;

final class Rarity extends AbstractRegisterable implements IRarity {

  private final TextColor textColor;
  private final double weight;
  private final String name;
  private final int priority;

  Rarity(Key key, TextColor textColor, double weight, String name, int priority) {
    super(key);
    this.textColor = textColor;
    this.weight = weight;
    this.name = name;
    this.priority = priority;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public TextColor getTextColor() {
    return textColor;
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public @NotNull Key key() {
    return key;
  }

  @Override
  public int compareTo(@NotNull IRarity o) {
    int comparison = Integer.compare(this.priority, o.getPriority());
    return comparison != 0 ? comparison : name.compareTo(o.getName());
  }

  @Override
  public String toString() {
    return new StringBuilder(this.getClass().getSimpleName())
        .append('[').append("key=").append(key.asString()).append(", ")
        .append("name=").append(name).append(", ")
        .append("base=").append(weight).append(", ")
        .append("priority=").append(priority).append(", ")
        .append("color=rgb(")
        .append(textColor.red()).append(", ")
        .append(textColor.green()).append(", ")
        .append(textColor.blue()).append(")")
        .append(']').toString();
  }
}