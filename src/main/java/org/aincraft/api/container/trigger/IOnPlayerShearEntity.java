package org.aincraft.api.container.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearEntityContext;

public interface IOnPlayerShearEntity {

  void onPlayerShear(IPlayerShearEntityContext context, EffectInstanceMeta meta);
}
