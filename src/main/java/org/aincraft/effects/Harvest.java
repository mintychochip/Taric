package org.aincraft.effects;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.IOnInteract;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.events.FakeBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

final class Harvest extends AbstractGemEffect implements IOnBlockBreak, IOnBlockDrop, IOnInteract {

  private final Set<Location> harvested = new HashSet<>();

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_BREAK, TargetType.HOE),
        Map.entry(TriggerType.BLOCK_DROP, TargetType.HOE),
        Map.entry(TriggerType.INTERACT, TargetType.HOE)
    );
  }

  @Override
  public void onInteract(IInteractContext context, int rank) {
    Block block = context.getBlock();
    Action action = context.getAction();
    if (block == null || (action.isLeftClick() || action == Action.PHYSICAL) || shouldNotHarvest(
        block)) {
      return;
    }
    harvested.add(block.getLocation());
    Bukkit.getPluginManager().callEvent(new FakeBlockBreakEvent(block, context.getPlayer()));
  }

  private static boolean shouldNotHarvest(@NotNull Block block) {
    BlockData blockData = block.getBlockData();
    if (!(blockData instanceof Ageable ageable)) {
      return true;
    }
    int age = ageable.getAge();
    int max = ageable.getMaximumAge();
    return age < max;
  }

  @Override
  public void onBlockDrop(IBlockDropContext context, int rank) {
    Block block = context.getBlock();
    Location location = block.getLocation();
    if (!harvested.contains(location)) {
      return;
    }
    harvested.remove(location);
    replant(block, context.getBlockState().getType());
  }

  private static void replant(@NotNull Block block, Material cropMaterial) {
    block.setType(cropMaterial);
    Ageable ageable = (Ageable) Bukkit.createBlockData(cropMaterial);
    ageable.setAge(0);
    block.setBlockData(ageable);
  }

  @Override
  public void onBlockBreak(IBlockBreakContext context, int rank, BlockFace blockFace) {
    if (shouldNotHarvest(context.getBlock())) {
      return;
    }
    harvested.add(context.getBlock().getLocation());
  }
}
