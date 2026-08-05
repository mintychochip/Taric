package org.aincraft.container.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class ExponentialRandomSelectorTest {

  @Test
  void constructor_baseNotGreaterThanOne_throws() {
    assertThrows(IllegalArgumentException.class, () -> new ExponentialRandomSelector<String>(1.0));
    assertThrows(IllegalArgumentException.class, () -> new ExponentialRandomSelector<String>(0.5));
  }

  @Test
  void getRandom_empty_throwsIllegalStateException() {
    ExponentialRandomSelector<String> selector = new ExponentialRandomSelector<>(2.0);
    RandomGenerator random = RandomGenerator.of("L64X128MixRandom");

    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> selector.getRandom(random));
    assertEquals("No elements to select from", thrown.getMessage());
  }

  @Test
  void getRandom_singleElement_returnsThatElement() {
    ExponentialRandomSelector<String> selector = new ExponentialRandomSelector<>(2.0);
    selector.add("only");

    RandomGenerator random = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public double nextDouble() {
        return 0.5;
      }
    };

    assertEquals("only", selector.getRandom(random));
  }

  @Test
  void getRandom_nearZero_prefersEarlierIndex() {
    ExponentialRandomSelector<String> selector = new ExponentialRandomSelector<>(2.0);
    selector.add("first");
    selector.add("second");
    selector.add("third");

    // weight[i] = base^(-i); index 0 has highest weight. r near 0 selects index 0.
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

    assertEquals("first", selector.getRandom(nearZero));
  }

  @Test
  void getRandom_returnsElementFromList() {
    ExponentialRandomSelector<String> selector = new ExponentialRandomSelector<>(3.0);
    selector.add("a");
    selector.add("b");

    RandomGenerator mid = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public double nextDouble() {
        return 0.5;
      }
    };

    String picked = selector.getRandom(mid);
    assertTrue(selector.contains(picked));
  }
}
