package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.BlockDropContext;

public interface IOnBlockDrop {

  void onBlockDrop(BlockDropContext context, EffectInstanceMeta meta);
}
