package org.aincraft.effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.context.IEntityKillContext;
import org.aincraft.api.trigger.IOnEntityKill;
import org.aincraft.api.trigger.IOnPlayerFish;
import org.aincraft.api.trigger.IOnPlayerShearEntity;
import org.aincraft.api.context.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.api.context.IPlayerFishContext;
import org.aincraft.api.trigger.TriggerTypes;
import org.aincraft.api.trigger.ITriggerType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

final class Scavenge extends AbstractGemEffect implements IOnEntityKill, IOnPlayerShearEntity,
    IOnPlayerFish {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_KILL, TargetType.RANGED_WEAPON),
        Map.entry(TriggerTypes.PLAYER_SHEAR_ENTITY, TypeSet.single(Material.SHEARS)),
        Map.entry(TriggerTypes.PLAYER_FISH, TypeSet.single(Material.FISHING_ROD))
    );
  }

  @Override
  public void onPlayerShear(IPlayerShearEntityContext context, EffectInstanceMeta meta) {
    context.setDrops(scavenge(meta.getRank(), context.getDrops()));
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

  private static int looting(int base, int level) {
    return base + Taric.getRandom().nextInt(level);
  }

  @Override
  public void onPlayerFish(IPlayerFishContext context, EffectInstanceMeta meta) {
    ItemStack drop = context.getDrops();
    if (drop == null) {
      return;
    }
    int base = drop.getAmount();
    int looting = looting(base, meta.getRank());
    drop.setAmount(looting);
  }

  @Override
  public void onKillEntity(IEntityKillContext context, EffectInstanceMeta meta) {
    context.setDrops(scavenge(meta.getRank(), context.getDrops()));
  }
}
