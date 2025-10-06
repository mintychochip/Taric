package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.BlockDropContext;
import org.aincraft.api.trigger.IOnBlockDrop;
import org.aincraft.api.trigger.TriggerType;
import org.bukkit.Material;

public class DustCollector extends AbstractGemEffect implements IOnBlockDrop {

  @Override
  protected Map<TriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public void onBlockDrop(BlockDropContext context, EffectInstanceMeta meta) {

  }
}
