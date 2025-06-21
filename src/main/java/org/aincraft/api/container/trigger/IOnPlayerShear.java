package org.aincraft.api.container.trigger;

import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearContext;

public interface IOnPlayerShear {
  void onPlayerShear(IPlayerShearContext context);
}
