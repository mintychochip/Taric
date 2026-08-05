package org.aincraft.container.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class UniformRandomSelectorTest {

  @Test
  void getRandom_empty_throwsIllegalStateException() {
    UniformRandomSelector<String> selector = new UniformRandomSelector<>();
    RandomGenerator random = RandomGenerator.of("L64X128MixRandom");

    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> selector.getRandom(random));
    assertEquals("no elements to select from", thrown.getMessage());
  }

  @Test
  void getRandom_singleElement_returnsThatElement() {
    UniformRandomSelector<String> selector = new UniformRandomSelector<>();
    selector.add("only");
    RandomGenerator random = () -> 0L;

    assertEquals("only", selector.getRandom(random));
  }

  @Test
  void getRandom_usesIndexFromRandomGenerator() {
    UniformRandomSelector<String> selector = new UniformRandomSelector<>();
    selector.add("a");
    selector.add("b");
    selector.add("c");

    // nextInt(bound) for RandomGenerator default uses nextLong; control via fixed nextInt override
    RandomGenerator picksIndexOne = new RandomGenerator() {
      @Override
      public long nextLong() {
        return 0L;
      }

      @Override
      public int nextInt(int bound) {
        return 1;
      }
    };

    assertEquals("b", selector.getRandom(picksIndexOne));
  }
}
