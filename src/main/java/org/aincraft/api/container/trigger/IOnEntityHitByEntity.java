package org.aincraft.api.container.trigger;

import org.aincraft.api.container.EffectInstanceMeta;

public interface IOnEntityHitByEntity {

  void onHitByEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta);
}
