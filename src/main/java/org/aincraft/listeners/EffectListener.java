package org.aincraft.listeners;


import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.launchable.ILaunchable;
import org.aincraft.api.context.IShootBowContext;
import org.aincraft.api.trigger.TriggerTypes;
import org.aincraft.container.context.DispatchContexts;
import org.aincraft.container.context.IDispatch;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.events.FakeBlockBreakEvent;
import org.aincraft.events.FakeBlockDropItemEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class EffectListener implements Listener {

  private final LoadingCache<LivingEntity, IGemInventory> inventoryCache;
  private final Set<Location> blocksDestroyed = new HashSet<>();
  private final IDispatch dispatch;

  @Inject
  public EffectListener(
      LoadingCache<LivingEntity, IGemInventory> inventoryCache,
      IDispatch dispatch) {
    this.inventoryCache = inventoryCache;
    this.dispatch = dispatch;
  }

  @SuppressWarnings("UnstableApiUsage")
  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onKillEntity(final EntityDeathEvent event) {
    DamageSource damageSource = event.getDamageSource();
    Entity causingEntity = damageSource.getCausingEntity();
    if (!(causingEntity instanceof LivingEntity livingEntity)) {
      return;
    }
    try {
      IGemInventory inventory = inventoryCache.get(livingEntity);
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.ENTITY_KILL);
      dispatch.dispatch(DispatchContexts.KILL_ENTITY, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }


  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onEntityBowShoot(final EntityShootBowEvent event) {
    LivingEntity shooter = event.getEntity();
    try {
      IGemInventory inventory = inventoryCache.get(shooter);
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.SHOOT_BOW);
      IShootBowContext context = dispatch.dispatch(DispatchContexts.SHOOT_BOW, loader, event,
          e -> {
            Projectile p = (Projectile) e.getProjectile();
            p.remove();
          });
      for (ILaunchable launchable : context.getLaunchables()) {
        launchable.launch(shooter);
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerHitEntity(final EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    if (!(damager instanceof Mob || damager instanceof Player)) {
      return;
    }
    if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
      return;
    }
    try {
      IGemInventory inventory = inventoryCache.get((LivingEntity) event.getDamager());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.ENTITY_HIT_ENTITY);
      dispatch.dispatch(DispatchContexts.ENTITY_HIT_ENTITY, loader, event);

      IGemInventory damagee = inventoryCache.get(livingEntity);
      loader = damagee.getLoader(TriggerTypes.ENTITY_HIT_BY_ENTITY);
      dispatch.dispatch(DispatchContexts.ENTITY_HIT_BY_ENTITY, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }


  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerShearEntity(final PlayerShearEntityEvent event) {
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.PLAYER_SHEAR_ENTITY);
      dispatch.dispatch(DispatchContexts.PLAYER_SHEAR_ENTITY, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockDropItem(final BlockDropItemEvent event) {
    if (event instanceof FakeBlockDropItemEvent) {
      return;
    }
    Block block = event.getBlock();
    Location location = block.getLocation();
    if (!blocksDestroyed.contains(location)) {
      return;
    }
    blocksDestroyed.remove(location);
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.BLOCK_DROP);
      dispatch.dispatch(DispatchContexts.BLOCK_DROP, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockBreak(final BlockBreakEvent event) {
    if (event instanceof FakeBlockBreakEvent) {
      return;
    }
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.BLOCK_BREAK);
      dispatch.dispatch(DispatchContexts.BLOCK_BREAK, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    blocksDestroyed.add(event.getBlock().getLocation());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onInteract(final PlayerInteractEvent event) {
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.INTERACT);
      dispatch.dispatch(DispatchContexts.INTERACT, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onPlayerFish(final PlayerFishEvent event) {
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.PLAYER_FISH);
      dispatch.dispatch(DispatchContexts.PLAYER_FISH, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  //
//
//  //TODO: fix api, usage is trash rn
//  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//  private void onBucketEmpty(final PlayerBucketEmptyEvent event) {
//    Player player = event.getPlayer();
//    try {
//      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.BUCKET_EMPTY,
//          inventoryCache.get(player));
//      if (queue.isEmpty()) {
//        return;
//      }
//      EquipmentSlot hand = event.getHand();
//      ItemStack bucket = event.getItemStack();
//      Mutable<ItemStack> after = new Mutable<>(
//          bucket != null ? bucket.clone() : new ItemStack(Material.AIR));
//      for (EffectInstance instance : queue) {
//        if (instance.getEffect() instanceof IOnBucketEmpty trigger) {
//          trigger.onBucketEmpty(instance.getRank(), player, bucket, event.getBucket(), hand, after);
//        }
//      }
//      ItemStack newStack = after.get();
//      if (!newStack.getType().isAir()) {
//        event.setItemStack(newStack);
//      }
//    } catch (ExecutionException e) {
//      throw new RuntimeException(e);
//    }
//  }
//  /*
//  Semantics of onActivate, the only effect being checked is the hand
//   */
//
//  @EventHandler(priority = EventPriority.MONITOR)
//  private void onActivate(final PlayerInteractEvent event) {
//    if (event.getAction() == Action.PHYSICAL) {
//      return;
//    }
//    EquipmentSlot hand = event.getHand();
//    if (hand == EquipmentSlot.OFF_HAND) {
//      return;
//    }
//    ItemStack item = event.getItem();
//    if (item == null || item.getType().isAir()) {
//      return;
//    }
//    Material material = item.getType();
//    Action action = event.getAction();
//    boolean isRanged = TargetType.RANGED_WEAPON.contains(material);
//    if ((action.isRightClick() && isRanged) || (!isRanged && action.isLeftClick())) {
//      return;
//    }
//    if (!TargetType.ALL.contains(material)) {
//      return;
//    }
//    Player player = event.getPlayer();
//    //TODO: revise this method, it only looks at mainhand
//    CompletableFuture.runAsync(() -> {
//      try {
//        EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.ACTIVATE,
//            inventoryCache.get(player));
//        if (queue.isEmpty()) {
//          effectQueuePool.release(queue);
//          return;
//        }
//        for (EffectInstance instance : queue) {
//          if (!(instance.getEffect() instanceof IOnActivate trigger)) {
//            continue;
//          }
//
//          Duration cooldown = trigger.getActivationCooldown();
//          EffectCooldown cd = cooldownDatabase.getCooldown(player, instance.getEffect());
//
//          if (cd == null) {
//            cd = cooldownDatabase.createCooldown(player, instance.getEffect());
//          }
//
//          if (!cd.isOnCooldown(cooldown)) {
//            Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
//              if (trigger.onActivate(instance.getRank(), player, item)) {
//                Bukkit.getAsyncScheduler().runNow(plugin, task -> {
//                  cooldownDatabase.updateCooldown(player, instance.getEffect());
//                });
//              }
//            });
//          } else {
//            EffectCooldown finalCd = cd;
//            Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
//              Bukkit.broadcast(Component.text(
//                  "Effect is on cooldown: " + finalCd.remaining(cooldown).toString()));
//            });
//          }
//        }
//        effectQueuePool.release(queue);
//      } catch (ExecutionException e) {
//        throw new RuntimeException(e);
//      }
//    });
//  }
//
  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onPlayerItemDamage(final PlayerItemDamageEvent event) {
    try {
      IGemInventory inventory = inventoryCache.get(event.getPlayer());
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.PLAYER_ITEM_DAMAGE);
      dispatch.dispatch(DispatchContexts.PLAYER_ITEM_DAMAGE, loader, event);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
