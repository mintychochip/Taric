package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.BlockBreakContext;

public interface IOnBlockBreak {

  void onBlockBreak(BlockBreakContext context, EffectInstanceMeta meta);
}
