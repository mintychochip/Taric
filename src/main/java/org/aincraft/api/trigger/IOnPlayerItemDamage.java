package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IItemDamageContext.PlayerItemDamageContext;

public interface IOnPlayerItemDamage {

  void onPlayerItemDamage(PlayerItemDamageContext context, EffectInstanceMeta meta);
}
