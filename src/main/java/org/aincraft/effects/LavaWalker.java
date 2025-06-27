package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.context.IEntityMoveContext;
import org.aincraft.api.context.IPlayerMoveContext;
import org.aincraft.api.trigger.IOnEntityMove;
import org.aincraft.api.trigger.IOnPlayerMove;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Location;
import org.bukkit.Material;

public class LavaWalker extends AbstractGemEffect implements IOnEntityMove, IOnPlayerMove {

  @Override
  public void onEntityMove(IEntityMoveContext context, EffectInstanceMeta meta) {
    Location from = context.getFrom();
  }


  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.ENTITY_MOVE, TargetType.BOOTS),
        Map.entry(TriggerTypes.PLAYER_MOVE, TargetType.BOOTS)
    );
  }

  @Override
  public void onPlayerMove(IPlayerMoveContext context, EffectInstanceMeta meta) {
    Location from = context.getFrom();
  }
}
