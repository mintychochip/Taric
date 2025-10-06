package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.PlayerMoveContext;

public interface IOnPlayerMove {

  void onPlayerMove(PlayerMoveContext context, EffectInstanceMeta meta);
}
