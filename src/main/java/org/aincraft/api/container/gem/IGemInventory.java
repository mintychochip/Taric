package org.aincraft.api.container.gem;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.IQueueLoader.IQueueLoaderHolder;
import org.aincraft.effects.EffectQueuePool.EffectInstance;

public interface IGemInventory extends IQueueLoaderHolder<EffectInstance> {
  IEquipment getEquipment();
}
