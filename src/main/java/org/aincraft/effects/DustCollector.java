package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.IBlockDropContext;
import org.aincraft.api.trigger.IOnBlockDrop;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Material;

public class DustCollector extends AbstractGemEffect implements IOnBlockDrop {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public void onBlockDrop(IBlockDropContext context, EffectInstanceMeta meta) {

  }
}
