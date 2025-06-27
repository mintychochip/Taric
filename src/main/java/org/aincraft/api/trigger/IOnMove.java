package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IMoveContext;

public interface IOnMove {

  void onMove(IMoveContext context, EffectInstanceMeta meta);
}
