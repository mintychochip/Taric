package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.context.IExperienceContext;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnFish;
import org.aincraft.api.container.trigger.IOnKillEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

final class Insight extends AbstractGemEffect implements IOnKillEntity, IOnBlockBreak, IOnFish {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.KILL_ENTITY, TargetType.MELEE_WEAPON),
        Map.entry(TriggerType.BLOCK_BREAK, TargetType.PICKAXE),
        Map.entry(TriggerType.FISH, TypeSet.single(Material.FISHING_ROD))
    );
  }

  @Override
  public void onKillEntity(IKillEntityContext context, int rank) {
    echoes(context, rank);
  }

  private static void echoes(IExperienceContext context, int rank) {
    if (context.getExperience() <= 0) {
      return;
    }
    int base = context.getExperience();
    int bonusOrbs = Taric.getRandom()
        .nextInt(Settings.KNOWLEDGE_ORBS_MIN, Settings.KNOWLEDGE_ORBS_MAX);
    int total = base + rank * bonusOrbs;
    context.setExperience(total);
  }

  @Override
  public void onFish(IFishContext context, int rank) {
    echoes(context, rank);
  }

  @Override
  public void onBlockBreak(IBlockBreakContext context, int rank, BlockFace blockFace) {
    echoes(context, rank);
  }
}
