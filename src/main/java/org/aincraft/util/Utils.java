package org.aincraft.util;

public class Utils {
  public static String toTitleCase(String input) {
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
}
