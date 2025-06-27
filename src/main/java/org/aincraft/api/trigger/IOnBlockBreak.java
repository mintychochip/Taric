package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IBlockBreakContext;

public interface IOnBlockBreak {

  void onBlockBreak(IBlockBreakContext context, EffectInstanceMeta meta);
}
