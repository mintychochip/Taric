package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.context.IExperienceContext;
import org.aincraft.api.context.IBlockBreakContext;
import org.aincraft.api.context.IEntityKillContext;
import org.aincraft.api.trigger.IOnBlockBreak;
import org.aincraft.api.trigger.IOnEntityKill;
import org.aincraft.api.trigger.IOnPlayerFish;
import org.aincraft.api.context.IPlayerFishContext;
import org.aincraft.api.trigger.TriggerTypes;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Material;

final class Insight extends AbstractGemEffect implements IOnEntityKill, IOnBlockBreak,
    IOnPlayerFish {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_KILL, TargetType.MELEE_WEAPON),
        Map.entry(TriggerTypes.BLOCK_BREAK, TargetType.PICKAXE),
        Map.entry(TriggerTypes.PLAYER_FISH, TypeSet.single(Material.FISHING_ROD))
    );
  }

  @Override
  public void onBlockBreak(IBlockBreakContext context, EffectInstanceMeta meta) {
    echoes(context, meta.getRank());
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
  public void onPlayerFish(IPlayerFishContext context, EffectInstanceMeta meta) {
    echoes(context, meta.getRank());
  }


  @Override
  public void onKillEntity(IEntityKillContext context, EffectInstanceMeta meta) {
    echoes(context, meta.getRank());
  }
}
