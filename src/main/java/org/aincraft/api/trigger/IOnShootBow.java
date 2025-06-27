package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IShootBowContext;

public interface IOnShootBow {

  void onShootBow(IShootBowContext context, EffectInstanceMeta meta);
}
