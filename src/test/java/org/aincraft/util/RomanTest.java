package org.aincraft.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RomanTest {

  @Test
  void fromInteger_four_returnsIV() {
    assertEquals("IV", Roman.fromInteger(4));
  }

  @ParameterizedTest
  @CsvSource({
      "1, I",
      "3, III",
      "4, IV",
      "9, IX",
      "10, X",
      "40, XL",
      "44, XLIV",
      "50, L",
      "90, XC",
      "99, XCIX",
      "100, C",
      "400, CD",
      "500, D",
      "900, CM",
      "1000, M",
      "1984, MCMLXXXIV",
      "3999, MMMCMXCIX"
  })
  void fromInteger_knownValues(int number, String expected) {
    assertEquals(expected, Roman.fromInteger(number));
  }

  @Test
  void fromInteger_zero_returnsEmpty() {
    assertEquals("", Roman.fromInteger(0));
  }
}
