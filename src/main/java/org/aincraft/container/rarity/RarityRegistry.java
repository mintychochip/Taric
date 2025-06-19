package org.aincraft.container.rarity;

import java.util.Collection;
import org.aincraft.Taric;
import org.aincraft.api.container.IRarity;
import org.aincraft.registry.IRegistry;
import org.aincraft.registry.SharedRegistry;

final class RarityRegistry extends SharedRegistry<IRarity> {

  @Override
  public synchronized IRegistry<IRarity> register(IRarity object) throws IllegalArgumentException {
    Collection<IRarity> values = this.values();
    double sum = 0;
    for (IRarity rarity : values) {
      sum += rarity.getBase();
    }
    Taric.getLogger().info(sum + "");
    if (object.getBase() + sum > 1.0) {
      throw new IllegalArgumentException("failed to register: %s".formatted(object.getName()) + " the sum is greater than 1.0");
    }
    return super.register(object);
  }

}
