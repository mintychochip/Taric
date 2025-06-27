package org.aincraft.api.container;

import org.aincraft.effects.IGemEffect;

public interface IEffectInstance {

  IGemEffect getEffect();

  EffectInstanceMeta getMeta();
}
