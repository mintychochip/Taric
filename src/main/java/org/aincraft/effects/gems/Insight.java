package org.aincraft.effects.gems;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.container.TargetType;
import org.aincraft.effects.triggers.IOnBlockBreak;
import org.aincraft.effects.triggers.IOnKillEntity;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.util.Mutable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Insight extends AbstractGemEffect implements IOnKillEntity, IOnBlockBreak {

  public Insight(String key) {
    super(key);
  }

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
  public void onKillEntity(int rank, DamageSource damageSource, LivingEntity entity,
      Mutable<Integer> experience, List<ItemStack> drops) {
    experience.set(echoes(rank, experience.get()));
  }

  @Override
  public void onBlockBreak(int rank, Player player, ItemStack tool, BlockFace hitFace, Block block,
      Mutable<Integer> experience) {
    if (experience.get() <= 0) {
      return;
    }
    experience.set(echoes(experience.get(), rank));
  }
}
