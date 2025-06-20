package org.aincraft.effects.gems;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.Mutable;
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
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class Harvest extends AbstractGemEffect implements IOnBlockBreak, IOnBlockDrop, IOnInteract {

  private final Set<Location> harvested = new HashSet<>();

  @Override
  public void onBlockBreak(IBlockBreakReceiver receiver) {
    if (shouldNotHarvest(receiver.getBlock())) {
      return;
    }
    harvested.add(receiver.getBlock().getLocation());
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_BREAK, TargetType.HOE),
        Map.entry(TriggerType.BLOCK_DROP, TargetType.HOE),
        Map.entry(TriggerType.INTERACT, TargetType.HOE)
    );
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

  private static void replant(@NotNull Block block, Material cropMaterial) {
    block.setType(cropMaterial);
    Ageable ageable = (Ageable) Bukkit.createBlockData(cropMaterial);
    ageable.setAge(0);
    block.setBlockData(ageable);
  }

  @Override
  public void onBlockDrop(IBlockDropReceiver receiver) {
    Block block = receiver.getBlock();
    Location location = block.getLocation();
    if (!harvested.contains(location)) {
      return;
    }
    harvested.remove(location);
    replant(block, receiver.getBlockState().getType());
  }

  @Override
  public void onInteract(IInteractReceiver receiver) {
    Block block = receiver.getBlock();
    Action action = receiver.getAction();
    if (block == null || (action.isLeftClick() || action == Action.PHYSICAL) || shouldNotHarvest(
        block)) {
      return;
    }
    harvested.add(block.getLocation());
    Bukkit.getPluginManager().callEvent(new FakeBlockBreakEvent(block, receiver.getPlayer()));
  }
}
