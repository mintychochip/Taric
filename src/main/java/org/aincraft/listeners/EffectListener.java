package org.aincraft.listeners;


import static org.bukkit.event.EventPriority.HIGH;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import net.kyori.adventure.text.Component;
import org.aincraft.api.container.Mutable;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.launchable.ILaunchable;
import org.aincraft.api.container.trigger.IOnActivate;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.IOnBucketEmpty;
import org.aincraft.api.container.trigger.IOnEntityHitEntity;
import org.aincraft.api.container.trigger.IOnEntityHitEntity.IEntityHitEntityReceiver;
import org.aincraft.api.container.trigger.IOnInteract;
import org.aincraft.api.container.trigger.IOnKillEntity;
import org.aincraft.api.container.trigger.IOnPlayerShear;
import org.aincraft.api.container.trigger.IOnShootBow;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.container.EffectCooldown;
import org.aincraft.container.launchable.LaunchableFactory;
import org.aincraft.container.trigger.BlockBreakReceiver;
import org.aincraft.container.trigger.BlockDropReceiver;
import org.aincraft.container.trigger.EntityHitEntityReceiver;
import org.aincraft.container.trigger.InteractReceiver;
import org.aincraft.container.trigger.KillTriggerReceiver;
import org.aincraft.container.trigger.PlayerShearEntityReceiver;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.EffectQueuePool.EffectQueue;
import org.aincraft.events.FakeBlockBreakEvent;
import org.aincraft.events.FakeBlockDropItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class EffectListener implements Listener {

  private final Shared shared;
  private final Plugin plugin;
  private final EffectQueuePool<EffectInstance> effectQueuePool;
  private final Map<Player, BlockFace> lastClickedFace = new HashMap<>();
  private final LoadingCache<LivingEntity, IGemInventory> inventoryCache;
  private final Set<Location> blocksDestroyed = new HashSet<>();
  private final IDatabase cooldownDatabase;


  @Inject
  public EffectListener(Shared shared, Plugin plugin,
      EffectQueuePool<EffectInstance> effectQueuePool,
      LoadingCache<LivingEntity, IGemInventory> inventoryCache, IDatabase cooldownDatabase) {
    this.shared = shared;
    this.plugin = plugin;
    this.effectQueuePool = effectQueuePool;
    this.inventoryCache = inventoryCache;
    this.cooldownDatabase = cooldownDatabase;
  }

  @EventHandler(priority = HIGH, ignoreCancelled = true)
  private void checkFaceBeforeBlockBreak(final PlayerInteractEvent event) {
    Action action = event.getAction();
    if (!action.isLeftClick()) {
      return;
    }
    lastClickedFace.put(event.getPlayer(), event.getBlockFace());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onKillEntity(final EntityDeathEvent event) {
    DamageSource damageSource = event.getDamageSource();
    if (!(damageSource.getCausingEntity() instanceof Player player)) {
      return;
    }
    KillTriggerReceiver receiver = new KillTriggerReceiver();
    receiver.setHandle(event);
    try {

      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
          TriggerType.KILL_ENTITY, inventoryCache.get(player));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }

      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnKillEntity trigger) {
          receiver.setRank(instance.getRank());
          trigger.onKillEntity(receiver);
        }
      }
      effectQueuePool.release(queue);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onEntityBowShoot(final EntityShootBowEvent event) {
    LivingEntity shooter = event.getEntity();
    if (!(event.getProjectile() instanceof Projectile projectile)) {
      return;
    }
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.SHOOT_BOW,
          inventoryCache.get(shooter));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }
      List<ILaunchable> launchables = new ArrayList<>();
      launchables.add(LaunchableFactory.create(projectile));
      projectile.remove();
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnShootBow trigger) {
          trigger.onShootBow(instance.getRank(), shooter, launchables);
        }
      }
      effectQueuePool.release(queue);
      for (ILaunchable launchable : launchables) {
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
    EntityHitEntityReceiver receiver = new EntityHitEntityReceiver();
    receiver.setHandle(event);
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
          TriggerType.ENTITY_HIT_ENTITY, inventoryCache.get((LivingEntity) event.getDamager()));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnEntityHitEntity trigger) {
          receiver.setRank(instance.getRank());
          trigger.onHitEntity(receiver);
        }
      }
      effectQueuePool.release(queue);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }


  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerShearEntity(final PlayerShearEntityEvent event) {
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.PLAYER_SHEAR,
          inventoryCache.get(event.getPlayer()));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }
      PlayerShearEntityReceiver receiver = new PlayerShearEntityReceiver();
      receiver.setHandle(PlayerShearEntityReceiver.createEvent(event));
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnPlayerShear trigger) {
          receiver.setRank(instance.getRank());
          trigger.onPlayerShear(receiver);
        }
      }
      effectQueuePool.release(queue);
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
    BlockDropReceiver receiver = new BlockDropReceiver();
    receiver.setHandle(event);
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.BLOCK_DROP,
          inventoryCache.get(event.getPlayer()));
      if (!queue.isEmpty()) {
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnBlockDrop trigger) {
            receiver.setRank(instance.getRank());
            trigger.onBlockDrop(receiver);
          }
        }
      }
      effectQueuePool.release(queue);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockBreak(final BlockBreakEvent event) {
    if (event instanceof FakeBlockBreakEvent) {
      return;
    }
    Player player = event.getPlayer();
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.BLOCK_BREAK,
          inventoryCache.get(player));
      if (!queue.isEmpty()) {
        BlockBreakReceiver receiver = new BlockBreakReceiver();
        receiver.setHandle(event);
        receiver.setBlockFace(lastClickedFace.get(player));
        receiver.setInitial(true);
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnBlockBreak trigger) {
            receiver.setRank(instance.getRank());
            trigger.onBlockBreak(receiver);
          }
        }
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    blocksDestroyed.add(event.getBlock().getLocation());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onInteract(final PlayerInteractEvent event) {
    Player player = event.getPlayer();
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
          TriggerType.INTERACT, inventoryCache.get(player));
      if (!queue.isEmpty()) {
        InteractReceiver receiver = new InteractReceiver();
        receiver.setHandle(event);
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnInteract trigger) {
            receiver.setRank(instance.getRank());
            trigger.onInteract(receiver);
          }
        }
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  //TODO: fix api, usage is trash rn
  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBucketEmpty(final PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.BUCKET_EMPTY,
          inventoryCache.get(player));
      if (queue.isEmpty()) {
        return;
      }
      EquipmentSlot hand = event.getHand();
      ItemStack bucket = event.getItemStack();
      Mutable<ItemStack> after = new Mutable<>(
          bucket != null ? bucket.clone() : new ItemStack(Material.AIR));
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnBucketEmpty trigger) {
          trigger.onBucketEmpty(instance.getRank(), player, bucket, event.getBucket(), hand, after);
        }
      }
      ItemStack newStack = after.get();
      if (!newStack.getType().isAir()) {
        event.setItemStack(newStack);
      }
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
  /*
  Semantics of onActivate, the only effect being checked is the hand
   */

  @EventHandler(priority = EventPriority.MONITOR)
  private void onActivate(final PlayerInteractEvent event) {
    if (event.getAction() == Action.PHYSICAL) {
      return;
    }
    EquipmentSlot hand = event.getHand();
    if (hand == EquipmentSlot.OFF_HAND) {
      return;
    }
    ItemStack item = event.getItem();
    if (item == null || item.getType().isAir()) {
      return;
    }
    Material material = item.getType();
    Action action = event.getAction();
    boolean isRanged = TargetType.RANGED_WEAPON.contains(material);
    if ((action.isRightClick() && isRanged) || (!isRanged && action.isLeftClick())) {
      return;
    }
    if (!TargetType.ALL.contains(material)) {
      return;
    }
    Player player = event.getPlayer();
    //TODO: revise this method, it only looks at mainhand
    CompletableFuture.runAsync(() -> {
      try {
        EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.ACTIVATE,
            inventoryCache.get(player));
        if (queue.isEmpty()) {
          effectQueuePool.release(queue);
          return;
        }
        for (EffectInstance instance : queue) {
          if (!(instance.getEffect() instanceof IOnActivate trigger)) {
            continue;
          }

          Duration cooldown = trigger.getActivationCooldown();
          EffectCooldown cd = cooldownDatabase.getCooldown(player, instance.getEffect());

          if (cd == null) {
            cd = cooldownDatabase.createCooldown(player, instance.getEffect());
          }

          if (!cd.isOnCooldown(cooldown)) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
              if (trigger.onActivate(instance.getRank(), player, item)) {
                Bukkit.getAsyncScheduler().runNow(plugin, task -> {
                  cooldownDatabase.updateCooldown(player, instance.getEffect());
                });
              }
            });
          } else {
            EffectCooldown finalCd = cd;
            Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
              Bukkit.broadcast(Component.text(
                  "Effect is on cooldown: " + finalCd.remaining(cooldown).toString()));
            });
          }
        }
        effectQueuePool.release(queue);
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
