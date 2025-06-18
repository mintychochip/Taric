package org.aincraft.api.exceptions;

import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;

public final class TargetTypeException extends RuntimeException {

  private final IGemEffect effect;
  private final Material material;

  public TargetTypeException(IGemEffect effect, Material material) {
    super("material: %s is not applicable to effect: %s".formatted(material,
        effect.getName()));
    this.effect = effect;
    this.material = material;
  }

  public Material getMaterial() {
    return material;
  }

  public IGemEffect getEffect() {
    return effect;
  }
}
