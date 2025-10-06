package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.EntityKillContext;

public interface IOnEntityKill {

  void onKillEntity(EntityKillContext context, EffectInstanceMeta meta);
}
