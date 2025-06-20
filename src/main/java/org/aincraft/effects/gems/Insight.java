package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.Mutable;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnKillEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Insight extends AbstractGemEffect implements IOnKillEntity, IOnBlockBreak {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.KILL_ENTITY, TargetType.MELEE_WEAPON),
        Map.entry(TriggerType.BLOCK_BREAK, TargetType.PICKAXE)
    );
  }

  private static int getBonusOrbs() {
    return Taric.getRandom().nextInt(Settings.KNOWLEDGE_ORBS_MIN, Settings.KNOWLEDGE_ORBS_MAX);
  }

  private static int echoes(int base, int rank) {
    return base + rank * getBonusOrbs();
  }

  @Override
  public void onKillEntity(IKillEntityReceiver receiver) {
    receiver.setExperience(echoes(receiver.getRank(),receiver.getExperience()));
  }

  @Override
  public void onBlockBreak(IBlockBreakReceiver receiver) {
    if (receiver.getExperience() <= 0)
      return;
    receiver.setExperience(echoes(receiver.getExperience(),receiver.getRank()));
  }
}
