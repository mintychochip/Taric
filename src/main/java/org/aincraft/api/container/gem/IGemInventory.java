package org.aincraft.api.container.gem;

import org.aincraft.api.container.Equipment;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.container.context.IEffectQueueLoader;

public interface IGemInventory {

  Equipment getEquipment();

  IEffectQueueLoader getLoader(TriggerType<?> trigger);
}
