package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IPlayerMoveContext;

public interface IOnPlayerMove {

  void onPlayerMove(IPlayerMoveContext context, EffectInstanceMeta meta);
}
