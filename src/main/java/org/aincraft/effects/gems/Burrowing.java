package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.container.Mutable;
import org.aincraft.container.TargetType;
import org.aincraft.container.TypeSet;
import org.aincraft.effects.triggers.IOnBlockBreak;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.events.FakeBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Burrowing extends AbstractGemEffect implements IOnBlockBreak {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_BREAK,
            TypeSet.builder().union(TargetType.PICKAXE, TargetType.SHOVEL).build())
    );
  }

  @Override
  public void onBlockBreak(int rank, Player player, ItemStack tool, BlockFace hitFace, Block origin,
      Mutable<Integer> experience) {
    Location location = origin.getLocation();
    for (int v = -rank; v <= rank; v++) {
      for (int u = -rank; u <= rank; u++) {
        if (u == 0 && v == 0) {
          continue;
        }

        Location target = switch (hitFace) {
          case UP, DOWN -> location.clone().add(u, 0, v);
          case EAST, WEST -> location.clone().add(0, -v, u);
          case NORTH, SOUTH -> location.clone().add(u, -v, 0);
          default -> null;
        };

        if (target != null) {
          Block block = target.getBlock();
          if (!block.getType().isAir() || block.isLiquid()) {
            Bukkit.getPluginManager().callEvent(new FakeBlockBreakEvent(block, player));
          }
        }
      }
    }
  }
}
