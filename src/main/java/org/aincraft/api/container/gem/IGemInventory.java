package org.aincraft.api.container.gem;

import org.aincraft.api.container.IEquipment;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.container.context.IEffectQueueLoader;

public interface IGemInventory {

  IEquipment getEquipment();

  IEffectQueueLoader getLoader(TriggerType<?> trigger);
}
