package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.EntityDamageEntityContext;

public interface IOnEntityHitByEntity {

  void onHitByEntity(EntityDamageEntityContext context, EffectInstanceMeta meta);
}
