package org.aincraft.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EffectCooldownTest {

  @Test
  void remaining_longElapsed_returnsZero() {
    UUID uuid = UUID.randomUUID();
    Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now().minusHours(2));
    EffectCooldown cooldown = new EffectCooldown(uuid, "blink", lastUsed);

    Duration remaining = cooldown.remaining(Duration.ofMinutes(5));

    assertEquals(Duration.ZERO, remaining);
    assertFalse(cooldown.isOnCooldown(Duration.ofMinutes(5)));
  }

  @Test
  void remaining_justUsed_isApproximatelyFullCooldown() {
    UUID uuid = UUID.randomUUID();
    Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now());
    EffectCooldown cooldown = new EffectCooldown(uuid, "crush", lastUsed);
    Duration cooldownDuration = Duration.ofMinutes(10);

    Duration remaining = cooldown.remaining(cooldownDuration);

    assertTrue(remaining.compareTo(Duration.ZERO) > 0);
    assertTrue(remaining.compareTo(cooldownDuration) <= 0);
    // Allow a few seconds of test execution skew
    assertTrue(remaining.compareTo(cooldownDuration.minusSeconds(5)) >= 0);
    assertTrue(cooldown.isOnCooldown(cooldownDuration));
  }

  @Test
  void remaining_partialElapsed_reducesRemaining() {
    UUID uuid = UUID.randomUUID();
    Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now().minusSeconds(30));
    EffectCooldown cooldown = new EffectCooldown(uuid, "flare", lastUsed);
    Duration cooldownDuration = Duration.ofSeconds(60);

    Duration remaining = cooldown.remaining(cooldownDuration);

    assertTrue(remaining.compareTo(Duration.ZERO) > 0);
    assertTrue(remaining.compareTo(Duration.ofSeconds(35)) <= 0);
    assertTrue(remaining.compareTo(Duration.ofSeconds(25)) >= 0);
    assertTrue(cooldown.isOnCooldown(cooldownDuration));
  }

  @Test
  void getters_returnConstructorValues() {
    UUID uuid = UUID.randomUUID();
    Timestamp lastUsed = Timestamp.valueOf(LocalDateTime.now());
    EffectCooldown cooldown = new EffectCooldown(uuid, "vampirism", lastUsed);

    assertEquals(uuid, cooldown.getUuid());
    assertEquals("vampirism", cooldown.getEffect());
    assertEquals(lastUsed, cooldown.getLastUsed());
  }

  @Test
  void setLastUsed_updatesTimestamp() {
    UUID uuid = UUID.randomUUID();
    Timestamp original = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
    EffectCooldown cooldown = new EffectCooldown(uuid, "blink", original);
    Timestamp updated = Timestamp.valueOf(LocalDateTime.now());

    cooldown.setLastUsed(updated);

    assertEquals(updated, cooldown.getLastUsed());
  }
}
