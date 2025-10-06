package org.aincraft.api.container.gem;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.api.trigger.TriggerType;

public interface IGemInventory {

  IEquipment getEquipment();

  IEffectQueueLoader getLoader(TriggerType<?> trigger);
}
