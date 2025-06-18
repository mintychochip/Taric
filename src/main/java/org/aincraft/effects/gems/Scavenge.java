package org.aincraft.effects.gems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.container.TargetType;
import org.aincraft.container.TypeSet;
import org.aincraft.effects.triggers.IOnKillEntity;
import org.aincraft.effects.triggers.IOnPlayerShear;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.util.Mutable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Scavenge extends AbstractGemEffect implements IOnKillEntity, IOnPlayerShear {

  private static int looting(int base, int level) {
    return base + Taric.getRandom().nextInt(level);
  }

  private static void scavenge(int rank, List<ItemStack> drops) {
    List<ItemStack> result = new ArrayList<>();
    for (ItemStack drop : drops) {
      int base = drop.getAmount();
      if (!Settings.SCAVENGE_BLACK_LIST.contains(drop.getType())) {
        result.add(new ItemStack(drop.getType(),
            Math.min(Math.max(base, looting(base, rank)), drop.getMaxStackSize())));
      } else {
        result.add(drop.clone());
      }
    }
    Bukkit.broadcastMessage(drops.toString() + " " + result.toString());
    drops.clear();
    drops.addAll(result);
  }

  Scavenge(String key) {
    super(key);
  }


  @Override
  public void onKillEntity(int rank, DamageSource damageSource, LivingEntity entity,
      Mutable<Integer> experience, List<ItemStack> drops) {
    scavenge(rank,drops);
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.KILL_ENTITY, TargetType.RANGED_WEAPON),
        Map.entry(TriggerType.PLAYER_SHEAR, TypeSet.single(Material.SHEARS))
    );
  }

  @Override
  public void onPlayerShear(int rank, Player player, Entity sheared, ItemStack tool,
      List<ItemStack> drops) {
    scavenge(rank,drops);
  }
}
