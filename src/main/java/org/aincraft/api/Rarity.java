package org.aincraft.api;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public record Rarity(Key key, TextColor textColor, double weight, String name, int priority,
                     double decayRate) implements Comparable<Rarity>, Keyed {

  @Override
  public int compareTo(@NotNull Rarity o) {
    int comparison = Integer.compare(this.priority, o.priority);
    return comparison != 0 ? comparison : name.compareTo(o.name);
  }
}
