package org.aincraft.effects;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.trigger.IOnActivate;
import org.aincraft.container.registerable.ITriggerType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

final class Blink extends AbstractGemEffect implements IOnActivate {
//
//  @Override
//  protected Map<TriggerType, Set<Material>> buildValidTargets() {
//    return Map.of(
//        TriggerType.ACTIVATE, TypeSet.builder().union(TargetType.TOOL, TargetType.WEAPON).build()
//    );
//  }


  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public Duration getActivationCooldown() {
    return Duration.ofSeconds(5);
  }

  @Override
  public boolean onActivate(int rank, Player player, ItemStack tool) {
    RayTraceResult result = player.rayTraceBlocks(rank * 50, FluidCollisionMode.SOURCE_ONLY);
    if (result == null) {
      return false;
    }
    Location from = player.getLocation();
    Location to = result.getHitPosition().toLocation(from.getWorld());
    to.setPitch(from.getPitch());
    to.setYaw(from.getYaw());
    return player.teleport(to);
  }

}
