package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.FishContext;

public interface IOnPlayerFish {

  void onPlayerFish(FishContext context, EffectInstanceMeta meta);
}
