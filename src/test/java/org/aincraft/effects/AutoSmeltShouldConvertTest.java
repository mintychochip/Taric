package org.aincraft.effects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

/**
 * Exercises the shipped {@link AutoSmelt#shouldConvert} decision used by the block-drop helper.
 */
class AutoSmeltShouldConvertTest {

  /** Deterministic RNG that always returns {@code value} from {@code nextInt}. */
  private static RandomGenerator fixed(int value) {
    return new RandomGenerator() {
      @Override
      public int nextInt(int bound) {
        return value;
      }

      @Override
      public long nextLong() {
        return value;
      }
    };
  }

  @Test
  void nonConvertible_neverConverts() {
    assertFalse(AutoSmelt.shouldConvert(false, 5, 5, fixed(0)));
  }

  @Test
  void invalidRanks_neverConvert() {
    assertFalse(AutoSmelt.shouldConvert(true, 0, 5, fixed(0)));
    assertFalse(AutoSmelt.shouldConvert(true, 3, 0, fixed(0)));
    assertFalse(AutoSmelt.shouldConvert(true, -1, 3, fixed(0)));
  }

  @Test
  void atOrAboveMaxRank_alwaysConverts() {
    assertTrue(AutoSmelt.shouldConvert(true, 5, 5, fixed(99)));
    assertTrue(AutoSmelt.shouldConvert(true, 6, 5, fixed(99)));
  }

  @Test
  void belowMaxRank_usesRandomAgainstMaxRank() {
    // nextInt(maxRank) < rank  →  1 < 2 when maxRank=4, rank=2
    assertTrue(AutoSmelt.shouldConvert(true, 2, 4, fixed(1)));
    // 3 < 2 is false
    assertFalse(AutoSmelt.shouldConvert(true, 2, 4, fixed(3)));
  }
}
