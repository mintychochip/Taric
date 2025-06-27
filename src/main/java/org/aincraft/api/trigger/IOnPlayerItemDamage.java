package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IItemDamageContext.IPlayerItemDamageContext;

public interface IOnPlayerItemDamage {

  void onPlayerItemDamage(IPlayerItemDamageContext context, EffectInstanceMeta meta);
}
