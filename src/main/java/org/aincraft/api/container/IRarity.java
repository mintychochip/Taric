package org.aincraft.api.container;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public interface IRarity extends Keyed, Comparable<IRarity> {

  int getPriority();

  TextColor getColor();

  double getBase();

  double additive(double chance);

  String getName();

  @Override
  int compareTo(@NotNull IRarity o);
}
