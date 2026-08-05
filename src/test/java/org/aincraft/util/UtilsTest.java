package org.aincraft.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UtilsTest {

  @Test
  void toTitleCase_hyphenatedWords_capitalizesEachSegment() {
    assertEquals("Vein Miner", Utils.toTitleCase("vein-miner"));
  }

  @Test
  void toTitleCase_singleWord_capitalizesFirstLetter() {
    assertEquals("Blink", Utils.toTitleCase("blink"));
  }

  @Test
  void toTitleCase_alreadyMixedCase_normalizesCasing() {
    assertEquals("Auto Smelt", Utils.toTitleCase("AUTO-SMELT"));
  }

  @Test
  void toTitleCase_null_returnsNull() {
    assertNull(Utils.toTitleCase(null));
  }

  @Test
  void toTitleCase_empty_returnsEmpty() {
    assertEquals("", Utils.toTitleCase(""));
  }

  @Test
  void toTitleCase_consecutiveHyphens_skipsEmptySegments() {
    assertEquals("A B", Utils.toTitleCase("a--b"));
  }
}
