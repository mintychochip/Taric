package org.aincraft.api.container.gem;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.dispatch.IEffectQueueLoader;
import org.aincraft.container.registerable.ITriggerType;

public interface IGemInventory {

  IEquipment getEquipment();

  IEffectQueueLoader getLoader(ITriggerType<?> trigger);
}
