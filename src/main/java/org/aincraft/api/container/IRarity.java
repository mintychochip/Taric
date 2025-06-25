package org.aincraft.api.container;

import java.util.random.RandomGenerator;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public interface IRarity extends Keyed, Comparable<IRarity>, IWeighable {

  int getPriority();

  @SuppressWarnings("unused")
  TextColor getTextColor();

  String getName();

  @Override
  int compareTo(@NotNull IRarity o);
}
