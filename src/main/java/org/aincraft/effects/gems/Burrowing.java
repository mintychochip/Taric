package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.Mutable;
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
  public void onBlockBreak(IBlockBreakReceiver receiver) {
    if (!receiver.isInitial()) {
      return;
    }
    Block origin = receiver.getBlock();
    int rank = receiver.getRank();
    BlockFace face = receiver.getBlockFace();
    Player player = receiver.getPlayer();
    Location location = origin.getLocation();
    for (int v = -rank; v <= rank; v++) {
      for (int u = -rank; u <= rank; u++) {
        if (u == 0 && v == 0) {
          continue;
        }

        Location target = switch (face) {
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
