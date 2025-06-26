package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.events.FakeBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

final class Burrowing extends AbstractGemEffect implements IOnBlockBreak {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_BREAK,
            TypeSet.builder().union(TargetType.PICKAXE, TargetType.SHOVEL).build())
    );
  }

  @Override
  public void onBlockBreak(IBlockBreakContext context, int rank, BlockFace blockFace) {
    if (!context.isFake()) {
      return;
    }
    Block origin = context.getBlock();
    Material material = origin.getType();
    float originHardness = material.getHardness();
    Player player = context.getPlayer();
    Location location = origin.getLocation();
    for (int v = -rank; v <= rank; v++) {
      for (int u = -rank; u <= rank; u++) {
        if (u == 0 && v == 0) {
          continue;
        }

        Location targetLocation = switch (blockFace) {
          case UP, DOWN -> location.clone().add(u, 0, v);
          case EAST, WEST -> location.clone().add(0, -v, u);
          case NORTH, SOUTH -> location.clone().add(u, -v, 0);
          default -> null;
        };

        if (targetLocation != null) {
          Block target = targetLocation.getBlock();
          Material targetMaterial = target.getType();
          if (!(target.getType().isAir() || target.isLiquid())
              && targetMaterial.getHardness() <= originHardness) {
            Bukkit.getPluginManager().callEvent(new FakeBlockBreakEvent(target, player));
          }
        }
      }
    }
  }
}
