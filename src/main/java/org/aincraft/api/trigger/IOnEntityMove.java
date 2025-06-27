package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IEntityMoveContext;

public interface IOnEntityMove {

  void onEntityMove(IEntityMoveContext context, EffectInstanceMeta meta);
}
