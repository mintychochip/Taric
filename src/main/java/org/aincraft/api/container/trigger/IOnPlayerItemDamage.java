package org.aincraft.api.container.trigger;

import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;

public interface IOnPlayerItemDamage {

  void onPlayerItemDamage(IPlayerItemDamageContext context, int rank);
}
