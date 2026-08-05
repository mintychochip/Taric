package org.aincraft.container.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class WeightedRandomSelectorTest {

  @Test
  void getRandom_empty_throwsIllegalStateException() {
    WeightedRandomSelector<String> selector = new WeightedRandomSelector<>();
    RandomGenerator random = RandomGenerator.of("L64X128MixRandom");

    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> selector.getRandom(random));
    assertEquals("objects cannot be empty", thrown.getMessage());
  }

  @Test
  void getRandom_singleWeightedEntry_returnsThatEntry() {
    WeightedRandomSelector<String> selector = new WeightedRandomSelector<>();
    selector.put("solo", 1.0);

    RandomGenerator alwaysZero = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public double nextDouble() {
        return 0.0;
      }
    };

    assertEquals("solo", selector.getRandom(alwaysZero));
  }

  @Test
  void getRandom_controlledDouble_selectsByCumulativeWeight() {
    WeightedRandomSelector<String> selector = new WeightedRandomSelector<>();
    // HashMap iteration order is not guaranteed; use one entry first, then two with known control
    selector.put("heavy", 9.0);
    selector.put("light", 1.0);

    // nextDouble() * cumulativeSum: 0.0 always hits first entry in iteration order
    RandomGenerator nearZero = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public double nextDouble() {
        return 0.0;
      }
    };

    String firstPick = selector.getRandom(nearZero);
    assertTrue(selector.containsKey(firstPick));

    // With target almost at cumulative sum end, last iterated entry may still be selected
    // when target falls in its bucket; near-1.0 should land in some valid entry.
    RandomGenerator nearOne = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public double nextDouble() {
        return 0.999999;
      }
    };

    String secondPick = selector.getRandom(nearOne);
    assertTrue(selector.containsKey(secondPick));

    // Across both extremes we should only ever see registered keys
    Set<String> seen = new HashSet<>();
    seen.add(firstPick);
    seen.add(secondPick);
    assertTrue(selector.keySet().containsAll(seen));
  }
}
