package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IBlockDropContext;

public interface IOnBlockDrop {

  void onBlockDrop(IBlockDropContext context, EffectInstanceMeta meta);
}
