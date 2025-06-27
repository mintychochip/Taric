package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IEntityItemDamageContext;

public interface IOnEntityItemDamage {

  void onEntityItemDamage(IEntityItemDamageContext context, EffectInstanceMeta meta);
}
