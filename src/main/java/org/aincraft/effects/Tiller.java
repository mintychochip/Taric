package org.aincraft.effects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;
import org.aincraft.api.container.trigger.IOnInteract;
import org.aincraft.api.container.trigger.IOnPlayerItemDamage;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class Tiller extends AbstractGemEffect implements IOnInteract, IOnPlayerItemDamage {

  private static final Set<Material> APPLICABLE = new HashSet<>();

  static {
    APPLICABLE.add(Material.DIRT);
    APPLICABLE.add(Material.DIRT_PATH);
    APPLICABLE.add(Material.GRASS_BLOCK);
    APPLICABLE.add(Material.COARSE_DIRT);
  }

  private final Map<Integer, Location> used = new HashMap<>();

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.INTERACT, TargetType.HOE),
        Map.entry(TriggerType.PLAYER_DAMAGE_ITEM, TargetType.HOE)
    );
  }

  @Override
  public void onInteract(IInteractContext context, int rank) {
    Block origin = context.getBlock();
    if (origin == null) {
      return;
    }
    Action action = context.getAction();
    if (action.isLeftClick() || action == Action.PHYSICAL) {
      return;
    }
    Material material = origin.getType();
    if (material.isAir() || !APPLICABLE.contains(material)) {
      return;
    }
    ItemStack item = context.getItem();
    if (item == null) {
      return;
    }
    int hash = item.hashCode();
    used.put(hash, origin.getLocation());
  }

  @Override
  public void onPlayerItemDamage(IPlayerItemDamageContext context, int rank) {
    ItemStack item = context.getItem();
    int hash = item.hashCode();
    if (!used.containsKey(hash)) {
      return;
    }
    Location location = used.remove(hash);
    for (int u = -rank; u <= rank; ++u) {
      for (int v = -rank; v <= rank; ++v) {
        for (int w = 1; w >= -1; --w) {
          if (u == 0 && v == 0 && w == 0) {
            continue;
          }
          Location target = location.clone().add(u, w, v);
          Block block = target.getBlock();
          Material blockMaterial = block.getType();
          if (!APPLICABLE.contains(blockMaterial)) {
            continue;
          }
          Block relative = block.getRelative(BlockFace.UP);
          if (!relative.getType().isAir()) {
            continue;
          }
          block.setType(blockMaterial == Material.COARSE_DIRT ? Material.DIRT : Material.FARMLAND);
          break;
        }
      }
    }
  }
}
