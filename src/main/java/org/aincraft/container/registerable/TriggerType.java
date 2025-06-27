package org.aincraft.container.registerable;

import java.util.Objects;
import net.kyori.adventure.key.Key;

final class TriggerType<T> extends AbstractRegisterable implements ITriggerType<T> {

  private final Class<T> clazz;

  public TriggerType(Key key, Class<T> clazz) {
    super(key);
    this.clazz = clazz;
  }

  @Override
  public Class<T> getTriggerClazz() {
    return clazz;
  }

  @Override
  public int hashCode() {
    return Objects.hash(clazz);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TriggerType<?> that = (TriggerType<?>) obj;
    return Objects.equals(this.clazz, that.clazz);
  }
}
