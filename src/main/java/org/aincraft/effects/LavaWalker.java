package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.context.EntityMoveContext;
import org.aincraft.api.context.PlayerMoveContext;
import org.aincraft.api.trigger.IOnEntityMove;
import org.aincraft.api.trigger.IOnPlayerMove;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Location;
import org.bukkit.Material;

public class LavaWalker extends AbstractGemEffect implements IOnEntityMove, IOnPlayerMove {

  @Override
  public void onEntityMove(EntityMoveContext context, EffectInstanceMeta meta) {
    Location from = context.getFrom();
  }


  @Override
  protected Map<TriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_MOVE, TargetType.BOOTS),
        Map.entry(TriggerTypes.PLAYER_MOVE, TargetType.BOOTS)
    );
  }

  @Override
  public void onPlayerMove(PlayerMoveContext context, EffectInstanceMeta meta) {
    Location from = context.getFrom();
  }
}
