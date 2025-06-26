package org.aincraft.effects;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.events.FakeBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

final class VeinMiner extends AbstractGemEffect implements IOnBlockBreak {

  private static final Set<Block> VISITED = new HashSet<>();
  private static final Deque<BlockVisit> STACK = new ArrayDeque<>();

  private record BlockVisit(Block block, int depth) {

  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_BREAK, TargetType.PICKAXE)
    );
  }

  @Override
  public void onBlockBreak(IBlockBreakContext context, int rank, BlockFace blockFace) {
    if (!context.isFake()) {
      return;
    }
    Block block = context.getBlock();
    Player player = context.getPlayer();
    Material material = block.getType();
    if (!material.toString().endsWith("_ORE")) {
      return;
    }
    if (!VISITED.isEmpty()) {
      VISITED.clear();
    }
    veinMine(VISITED, STACK, block, rank * Settings.VEIN_MINER_DEPTH_RANK, material);
    VISITED.remove(block);
    for (Block b : VISITED) {
      Bukkit.getPluginManager().callEvent(new FakeBlockBreakEvent(b, player));
    }
  }

  private static void veinMine(Set<Block> visited, Deque<BlockVisit> stack, Block start,
      int maxDepth, Material original) {
    stack.clear();
    stack.push(new BlockVisit(start, 0));
    while (!stack.isEmpty()) {
      BlockVisit visit = stack.pop();
      Block block = visit.block();
      int depth = visit.depth();

      Material material = block.getType();
      if (depth > maxDepth || material.isAir() || !material.toString().endsWith("_ORE")
          || visited.size() >= Settings.VEIN_MINER_MAX_BLOCKS || material != original) {
        continue;
      }

      if (!visited.add(block)) {
        continue; // Already visited
      }
      for (BlockFace face : BlockFace.values()) {
        if (!face.isCartesian()) {
          continue;
        }
        stack.push(new BlockVisit(block.getRelative(face), depth + 1));
      }
    }
  }
}
