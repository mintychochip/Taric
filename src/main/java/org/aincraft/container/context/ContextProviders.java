package org.aincraft.container.context;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.aincraft.api.context.BlockBreakContext;
import org.aincraft.api.context.BlockDropContext;
import org.aincraft.api.context.ContextFactory;
import org.aincraft.api.context.EntityDamageEntityContext;
import org.aincraft.api.context.EntityKillContext;
import org.aincraft.api.context.EntityMoveContext;
import org.aincraft.api.context.FishContext;
import org.aincraft.api.context.IItemDamageContext.EntityItemDamageContext;
import org.aincraft.api.context.IItemDamageContext.PlayerItemDamageContext;
import org.aincraft.api.context.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.api.context.IShootBowContext;
import org.aincraft.api.context.PlayerMoveContext;
import org.aincraft.api.trigger.IOnInteract.PlayerInteractContext;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class ContextProviders {

  public static final IContextProvider<EntityDamageEntityContext, EntityDamageByEntityEvent> ENTITY_DAMAGE_BY_ENTITY;
  public static final IContextProvider<EntityKillContext, EntityDeathEvent> ENTITY_KILL;
  public static final IContextProvider<FishContext, PlayerFishEvent> PLAYER_FISH;
  public static final IContextProvider<PlayerInteractContext, PlayerInteractEvent> INTERACT;
  public static final IContextProvider<BlockDropContext, BlockDropItemEvent> BLOCK_DROP;
  public static final IContextProvider<EntityItemDamageContext, EntityDamageItemEvent> ENTITY_ITEM_DAMAGE;
  public static final IContextProvider<PlayerItemDamageContext, PlayerItemDamageEvent> PLAYER_ITEM_DAMAGE;
  public static final IContextProvider<IPlayerShearEntityContext, PlayerShearEntityEvent> PLAYER_SHEAR_ENTITY;
  public static final IContextProvider<EntityMoveContext, EntityMoveEvent> ENTITY_MOVE;
  public static final IContextProvider<PlayerMoveContext, PlayerMoveEvent> PLAYER_MOVE;
  public static final IContextProvider<BlockBreakContext, BlockBreakEvent> BLOCK_BREAK;
  public static final IContextProvider<IShootBowContext, EntityShootBowEvent> SHOOT_BOW;

  static {
    ENTITY_DAMAGE_BY_ENTITY = ContextFactory::create;
    ENTITY_KILL = ContextFactory::create;
    PLAYER_FISH = ContextFactory::create;
    INTERACT = ContextFactory::create;
    BLOCK_DROP = ContextFactory::create;
    ENTITY_ITEM_DAMAGE = ContextFactory::create;

    PLAYER_ITEM_DAMAGE = ContextFactory::create;

    PLAYER_SHEAR_ENTITY = ContextFactory::create;

    ENTITY_MOVE = ContextFactory::create;

    PLAYER_MOVE = ContextFactory::create;

    BLOCK_BREAK = ContextFactory::create;

    SHOOT_BOW = ShootBowContext::new;
  }

  private ContextProviders() {
    throw new UnsupportedOperationException("This class should not be instantiated.");
  }

}
