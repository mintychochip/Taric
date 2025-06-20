package org.aincraft.effects.gems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnKillEntity;
import org.aincraft.api.container.trigger.IOnPlayerShear;
import org.aincraft.api.container.trigger.IShearEntityReceiver.IPlayerShearReceiver;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class Scavenge extends AbstractGemEffect implements IOnKillEntity, IOnPlayerShear {

  private static int looting(int base, int level) {
    return base + Taric.getRandom().nextInt(level);
  }

  private static List<ItemStack> scavenge(int rank, List<ItemStack> drops) {
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
    return result;
  }

  @Override
  public void onKillEntity(IKillEntityReceiver receiver) {
    receiver.setDrops(scavenge(receiver.getRank(),receiver.getDrops()));
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.KILL_ENTITY, TargetType.RANGED_WEAPON),
        Map.entry(TriggerType.PLAYER_SHEAR, TypeSet.single(Material.SHEARS))
    );
  }

  @Override
  public void onPlayerShear(IPlayerShearReceiver receiver) {
    receiver.setDrops(scavenge(receiver.getRank(),receiver.getDrops()));
  }
}
