package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IEntityKillContext;

public interface IOnEntityKill {

  void onKillEntity(IEntityKillContext context, EffectInstanceMeta meta);
}
