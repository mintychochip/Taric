package org.aincraft.container.registerable;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

abstract class AbstractRegisterable implements Keyed {

  protected final Key key;

  public AbstractRegisterable(Key key) {
    this.key = key;
  }

  static String toTitleCase(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    String[] split = input.split("-");
    StringBuilder sb = new StringBuilder();

    for (String str : split) {
      if (str.isEmpty()) {
        continue;
      }
      sb.append(Character.toUpperCase(str.charAt(0)))
          .append(str.substring(1).toLowerCase())
          .append(" ");
    }

    return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Keyed keyed)) {
      return false;
    }
    return this == obj || keyed.key().equals(key());
  }

  @Override
  public @NotNull Key key() {
    return key;
  }
}
