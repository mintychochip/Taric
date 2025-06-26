package org.aincraft.container.trigger;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;
import org.aincraft.api.container.trigger.IOnBlockBreak.IBlockBreakContext;
import org.aincraft.api.container.trigger.IOnBlockDrop.IBlockDropContext;
import org.aincraft.api.container.trigger.IOnEntityItemDamage.IEntityDamageItemContext;
import org.aincraft.api.container.trigger.IOnEntityMove.IEntityMoveContext;
import org.aincraft.api.container.trigger.IOnFish.IFishContext;
import org.aincraft.api.container.trigger.IOnInteract.IInteractContext;
import org.aincraft.api.container.trigger.IOnKillEntity.IKillEntityContext;
import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearContext;
import org.aincraft.events.FakeBlockBreakEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ContextProviders {

  public static final IContextProvider<IEntityDamageEntityContext, EntityDamageByEntityEvent> ENTITY_DAMAGE_BY_ENTITY;
  public static final IContextProvider<IKillEntityContext, EntityDeathEvent> KILL;
  public static final IContextProvider<IFishContext, PlayerFishEvent> FISH;
  public static final IContextProvider<IInteractContext, PlayerInteractEvent> INTERACT;
  public static final IContextProvider<IBlockDropContext, BlockDropItemEvent> BLOCK_DROP;
  public static final IContextProvider<IEntityDamageItemContext, EntityDamageItemEvent> ENTITY_DAMAGE_ITEM;
  public static final IContextProvider<IPlayerItemDamageContext, PlayerItemDamageEvent> PLAYER_ITEM_DAMAGE;
  public static final IContextProvider<IPlayerShearContext, PlayerShearEntityEvent> PLAYER_SHEAR;
  public static final IContextProvider<IEntityMoveContext, EntityMoveEvent> ENTITY_MOVE;
  public static final IContextProvider<IBlockBreakContext, BlockBreakEvent> BLOCK_BREAK;

  static {
    ENTITY_DAMAGE_BY_ENTITY = EntityDamageEntityContext::new;
    KILL = KillContext::new;
    FISH = FishContext::new;
    INTERACT = InteractContext::new;
    BLOCK_DROP = BlockDropContext::new;

    ENTITY_DAMAGE_ITEM = handle -> new EntityDamageItemContext(
        new EntityItemDamageEventDecorator(handle));

    PLAYER_ITEM_DAMAGE = handle -> new PlayerItemDamageContext(
        new PlayerItemDamageEventDecorator(handle));

    PLAYER_SHEAR = handle -> new PlayerShearEntityContext(
        new PlayerShearEntityEventDecorator(handle));

    ENTITY_MOVE = EntityMoveContext::new;

    BLOCK_BREAK = handle -> new BlockBreakContext(
        handle, handle instanceof FakeBlockBreakEvent);
  }

  private ContextProviders() {
    throw new UnsupportedOperationException("This class should not be instantiated.");
  }

}
