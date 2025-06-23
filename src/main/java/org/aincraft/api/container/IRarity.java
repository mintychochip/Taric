package org.aincraft.api.container;

import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public interface IRarity extends Keyed, Comparable<IRarity> {

  int getPriority();

  @SuppressWarnings("unused")
  TextColor getTextColor();

  double getWeight();

  String getName();

  @Override
  int compareTo(@NotNull IRarity o);
}
