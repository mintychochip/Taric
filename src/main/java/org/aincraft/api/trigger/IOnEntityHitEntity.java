package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IEntityDamageEntityContext;

public interface IOnEntityHitEntity {

  void onHitEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta);

}
