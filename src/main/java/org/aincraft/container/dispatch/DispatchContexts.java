package org.aincraft.container.dispatch;

import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnBlockBreak.IBlockBreakContext;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.IOnBlockDrop.IBlockDropContext;
import org.aincraft.api.container.trigger.IOnEntityHitByEntity;
import org.aincraft.api.container.trigger.IOnEntityHitEntity;
import org.aincraft.api.container.trigger.IOnEntityKill;
import org.aincraft.api.container.trigger.IOnEntityKill.IEntityKillContext;
import org.aincraft.api.container.trigger.IOnInteract;
import org.aincraft.api.container.trigger.IOnInteract.IInteractContext;
import org.aincraft.api.container.trigger.IOnPlayerFish;
import org.aincraft.api.container.trigger.IOnPlayerFish.IPlayerFishContext;
import org.aincraft.api.container.trigger.IOnPlayerShearEntity;
import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.container.context.ContextProviders;
import org.aincraft.container.context.IContextProvider;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.container.registerable.TriggerTypes;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public final class DispatchContexts {

  public static final IDispatchContext<IOnBlockBreak, IBlockBreakContext, BlockBreakEvent> BLOCK_BREAK = new DispatchContext<>(
      TriggerTypes.BLOCK_BREAK, ContextProviders.BLOCK_BREAK, IOnBlockBreak::onBlockBreak);
  public static final IDispatchContext<IOnBlockDrop, IBlockDropContext, BlockDropItemEvent> BLOCK_DROP = new DispatchContext<>(
      TriggerTypes.BLOCK_DROP, ContextProviders.BLOCK_DROP, IOnBlockDrop::onBlockDrop);
  public static final IDispatchContext<IOnInteract, IInteractContext, PlayerInteractEvent> INTERACT = new DispatchContext<>(
      TriggerTypes.INTERACT, ContextProviders.INTERACT, IOnInteract::onInteract);
  public static final IDispatchContext<IOnPlayerShearEntity, IPlayerShearEntityContext, PlayerShearEntityEvent> PLAYER_SHEAR_ENTITY = new DispatchContext<>(
      TriggerTypes.PLAYER_SHEAR_ENTITY, ContextProviders.PLAYER_SHEAR_ENTITY,
      IOnPlayerShearEntity::onPlayerShear);
  public static final IDispatchContext<IOnEntityHitEntity, IEntityDamageEntityContext, EntityDamageByEntityEvent> ENTITY_HIT_ENTITY = new DispatchContext<>(
      TriggerTypes.ENTITY_HIT_ENTITY, ContextProviders.ENTITY_DAMAGE_BY_ENTITY,
      IOnEntityHitEntity::onHitEntity);
  public static final IDispatchContext<IOnEntityHitByEntity, IEntityDamageEntityContext, EntityDamageByEntityEvent> ENTITY_HIT_BY_ENTITY = new DispatchContext<>(
      TriggerTypes.ENTITY_HIT_BY_ENTITY, ContextProviders.ENTITY_DAMAGE_BY_ENTITY,
      IOnEntityHitByEntity::onHitByEntity);
  public static final IDispatchContext<IOnPlayerFish, IPlayerFishContext, PlayerFishEvent> PLAYER_FISH = new DispatchContext<>(
      TriggerTypes.PLAYER_FISH, ContextProviders.PLAYER_FISH, IOnPlayerFish::onPlayerFish);

  public static final IDispatchContext<IOnEntityKill, IEntityKillContext, EntityDeathEvent> KILL_ENTITY = new DispatchContext<>(
      TriggerTypes.ENTITY_KILL, ContextProviders.ENTITY_KILL, IOnEntityKill::onKillEntity);

  private DispatchContexts() {
    throw new UnsupportedOperationException("This class should not be instantiated.");
  }

  @FunctionalInterface
  interface ITriggerExecutor<T, C> {

    void trigger(T trigger, C context, EffectInstanceMeta meta);
  }

  private record DispatchContext<T, C, E extends Event>(ITriggerType<T> triggerType,
                                                        IContextProvider<C, E> contextProvider,
                                                        ITriggerExecutor<T, C> triggerExecutor) implements
      IDispatchContext<T, C, E> {

    @Override
    public void trigger(T trigger, C context, EffectInstanceMeta meta) {
      triggerExecutor.trigger(trigger, context, meta);
    }
  }
}
