package org.aincraft.api.container.gem;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.api.trigger.ITriggerType;

public interface IGemInventory {

  IEquipment getEquipment();

  IEffectQueueLoader getLoader(ITriggerType<?> trigger);
}
