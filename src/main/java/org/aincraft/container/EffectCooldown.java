package org.aincraft.container;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class EffectCooldown {

  private final UUID uuid;
  private final String effect;
  private Timestamp lastUsed;

  public EffectCooldown(UUID uuid, String effect, Timestamp lastUsed) {
    this.uuid = uuid;
    this.effect = effect;
    this.lastUsed = lastUsed;
  }

  public void setLastUsed(Timestamp lastUsed) {
    this.lastUsed = lastUsed;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getEffect() {
    return effect;
  }

  public Timestamp getLastUsed() {
    return lastUsed;
  }

  public boolean isOnCooldown(Duration cooldown) {
    return remaining(cooldown).compareTo(Duration.ZERO) > 0;
  }

  public Duration remaining(Duration cooldown) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime lastUsedTime = this.lastUsed.toLocalDateTime();
    Duration elapsed = Duration.between(lastUsedTime, now);
    Duration remaining = cooldown.minus(elapsed);
    return remaining.isNegative() ? Duration.ZERO : remaining;
  }

}
