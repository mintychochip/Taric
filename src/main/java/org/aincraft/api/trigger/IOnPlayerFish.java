package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IPlayerFishContext;

public interface IOnPlayerFish {

  void onPlayerFish(IPlayerFishContext context, EffectInstanceMeta meta);
}
