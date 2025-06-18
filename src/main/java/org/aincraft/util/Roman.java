package org.aincraft.util;

public class Roman {

  public static String fromInteger(int number) {
    final int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    final String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV",
        "I"};

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < values.length; ++i) {
      if (number == 0) {
        break;
      }
      while (number >= values[i]) {
        sb.append(symbols[i]);
        number -= values[i];
      }
    }

    return sb.toString();
  }
}
