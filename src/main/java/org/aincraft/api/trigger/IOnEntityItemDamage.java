package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IItemDamageContext.EntityItemDamageContext;

public interface IOnEntityItemDamage {

  void onEntityItemDamage(EntityItemDamageContext context, EffectInstanceMeta meta);
}
