package org.aincraft.effects.triggers;

import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.Nullable;

public enum TriggerType {
  BLOCK_BREAK(IOnBlockBreak.class),
  BLOCK_DROP(IOnBlockDrop.class),
  ENTITY_HIT_ENTITY(IOnEntityHitEntity.class),
  KILL_ENTITY(IOnKillEntity.class),
  SHOOT_BOW(IOnShootBow.class),
  PLAYER_SHEAR(IOnPlayerShear.class),
  ACTIVATE(IOnActivate.class),
  SOCKET(IOnSocketEffect.class);

  private final Class<?> triggerClazz;

  TriggerType(Class<?> triggerClazz) {
    this.triggerClazz = triggerClazz;
  }

  @Nullable
  public static TriggerType find(Class<?> triggerClazz) {
    for (TriggerType triggerType : TriggerType.values()) {
      if (triggerType.is(triggerClazz)) {
        return triggerType;
      }
    }
    return null;
  }

  public boolean is(Class<?> triggerClazz) {
    return this.triggerClazz.equals(triggerClazz);
  }

  public Class<?> getTriggerClazz() {
    return triggerClazz;
  }

  public boolean hasTriggerType(IGemEffect effect) {
    return triggerClazz.isInstance(effect);
  }
}
