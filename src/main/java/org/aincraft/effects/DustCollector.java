package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;

public class DustCollector extends AbstractGemEffect implements IOnBlockDrop {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public void onBlockDrop(IBlockDropContext context, int rank) {

  }
}
