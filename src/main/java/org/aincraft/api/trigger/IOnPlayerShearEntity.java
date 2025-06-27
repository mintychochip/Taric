package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IShearEntityContext.IPlayerShearEntityContext;

public interface IOnPlayerShearEntity {

  void onPlayerShear(IPlayerShearEntityContext context, EffectInstanceMeta meta);
}
