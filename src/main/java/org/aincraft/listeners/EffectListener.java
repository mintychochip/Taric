package org.aincraft.listeners;


import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.container.dispatch.DispatchContexts;
import org.aincraft.container.dispatch.IDispatch;
import org.aincraft.container.dispatch.IEffectQueueLoader;
import org.aincraft.container.registerable.TriggerTypes;
import org.aincraft.database.IDatabase;
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
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.Plugin;

public class EffectListener implements Listener {

  private final Plugin plugin;
  private final LoadingCache<LivingEntity, IGemInventory> inventoryCache;
  private final Set<Location> blocksDestroyed = new HashSet<>();
  private final IDatabase cooldownDatabase;
  private final IDispatch dispatch;
  private final Map<LivingEntity, List<Projectile>> projectileStore = new HashMap<>();

  @Inject
  public EffectListener(Plugin plugin,
      LoadingCache<LivingEntity, IGemInventory> inventoryCache, IDatabase cooldownDatabase,
      IDispatch dispatch) {
    this.plugin = plugin;
    this.inventoryCache = inventoryCache;
    this.cooldownDatabase = cooldownDatabase;
    this.dispatch = dispatch;
  }

  //  @EventHandler(priority = HIGH, ignoreCancelled = true)
//  private void checkFaceBeforeBlockBreak(final PlayerInteractEvent event) {
//    Action action = event.getAction();
//    if (!action.isLeftClick()) {
//      return;
//    }
//    lastClickedFace.put(event.getPlayer(), event.getBlockFace());
//  }
//
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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

  //
//  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//  private void onEntityBowShoot(final EntityShootBowEvent event) {
//    LivingEntity shooter = event.getEntity();
//    if (!(event.getProjectile() instanceof Projectile projectile)) {
//      return;
//    }
//    List<Projectile> projectiles = projectileStore.computeIfAbsent(shooter,
//        key -> new ArrayList<>());
//    projectiles.clear();
//    try {
//      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.SHOOT_BOW,
//          inventoryCache.get(shooter));
//      if (!queue.isEmpty()) {
//        List<ILaunchable> launchables = new ArrayList<>();
//        launchables.add(LaunchableFactory.create(projectile));
//        projectile.remove();
//        for (EffectInstance instance : queue) {
//          if (instance.getEffect() instanceof IOnShootBow trigger) {
//            trigger.onShootBow(instance.getRank(), shooter, launchables);
//          }
//        }
//        for (ILaunchable launchable : launchables) {
//          Projectile launched = launchable.launch(shooter);
//          projectiles.add(launched);
//        }
//      }
//      effectQueuePool.release(queue);
//    } catch (ExecutionException e) {
//      throw new RuntimeException(e);
//    }
//  }
//
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
      IEffectQueueLoader loader = inventory.getLoader(TriggerTypes.BLOCK_BREAK);
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
//  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
//  private void onPlayerItemDamage(final PlayerItemDamageEvent event) {
//    IPlayerItemDamageContext context = ContextProviders.PLAYER_ITEM_DAMAGE.create(
//        event);
//    try {
//      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
//          TriggerType.PLAYER_DAMAGE_ITEM, inventoryCache.get(
//              event.getPlayer()));
//      if (!queue.isEmpty()) {
//        for (EffectInstance instance : queue) {
//          if (instance.getEffect() instanceof IOnPlayerItemDamage trigger) {
//            trigger.onPlayerItemDamage(context, instance.getRank());
//          }
//        }
//      }
//    } catch (ExecutionException e) {
//      throw new RuntimeException(e);
//    }
//  }
}
