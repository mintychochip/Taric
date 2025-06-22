package org.aincraft.container.rarity;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.aincraft.api.container.IRarity;
import org.jetbrains.annotations.NotNull;

final class Rarity extends AbstractRegisterable implements IRarity {

  private final TextColor textColor;
  private final double base;
  private final String name;
  private final int priority;

  Rarity(Key key, TextColor textColor, double base, String name, int priority) {
    super(key);
    this.textColor = textColor;
    this.base = base;
    this.name = name;
    this.priority = priority;
  }

  @Override
  public double additive(double chance) {
    return Math.min(1.0, chance + base);
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
  public double getBase() {
    return base;
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
        .append("base=").append(base).append(", ")
        .append("priority=").append(priority).append(", ")
        .append("color=rgb(")
          .append(textColor.red()).append(", ")
          .append(textColor.green()).append(", ")
          .append(textColor.blue()).append(")")
        .append(']').toString();
  }
}