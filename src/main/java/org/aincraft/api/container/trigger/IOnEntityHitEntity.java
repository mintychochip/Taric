package org.aincraft.api.container.trigger;

import org.aincraft.api.container.EffectInstanceMeta;

public interface IOnEntityHitEntity {

  void onHitEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta);

}
