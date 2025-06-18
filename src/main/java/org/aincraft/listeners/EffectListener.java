package org.aincraft.listeners;


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
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.container.EffectCooldown;
import org.aincraft.container.TargetType;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.ArrowLaunchable;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.EffectQueuePool.EffectQueue;
import org.aincraft.effects.triggers.IOnActivate;
import org.aincraft.effects.triggers.IOnBlockBreak;
import org.aincraft.effects.triggers.IOnBlockDrop;
import org.aincraft.effects.triggers.IOnEntityHitEntity;
import org.aincraft.effects.triggers.IOnKillEntity;
import org.aincraft.effects.triggers.IOnPlayerShear;
import org.aincraft.effects.triggers.IOnShootBow;
import org.aincraft.effects.triggers.IOnShootBow.ILaunchable;
import org.aincraft.effects.triggers.TriggerType;
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
import org.bukkit.entity.Arrow;
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

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
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
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
          TriggerType.KILL_ENTITY, inventoryCache.get(player));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }

      shared.getDrops().replaceAll(event.getDrops());
      shared.getExperience().set(event.getDroppedExp());
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnKillEntity trigger) {
          trigger.onKillEntity(instance.getRank(), damageSource, event.getEntity(),
              shared.getExperience(),
              shared.getDrops());
        }
      }
      effectQueuePool.release(queue);
      List<ItemStack> eventDrops = event.getDrops();
      eventDrops.clear();
      eventDrops.addAll(shared.getDrops());
      event.setDroppedExp(shared.getExperience().get());
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
      launchables.add(new ArrowLaunchable((Arrow) projectile));
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
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(
          TriggerType.ENTITY_HIT_ENTITY, inventoryCache.get((LivingEntity) event.getDamager()));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }
      @SuppressWarnings("deprecation")
      Map<DamageModifier, Double> modifiers = new HashMap<>();
      modifiers.put(DamageModifier.BASE, event.getDamage(DamageModifier.BASE));
      Map<DamageModifier, Function<? super Double, Double>> modifierFunctions = new HashMap<>();
      modifierFunctions.put(DamageModifier.BASE, Functions.identity());
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnEntityHitEntity trigger) {
          trigger.onHitEntity(instance.getRank(), event.getDamager(), event.getEntity(),
              modifiers);
        }
      }
      effectQueuePool.release(queue);
      for (Entry<DamageModifier, Double> entry : modifiers.entrySet()) {
        event.setDamage(entry.getKey(), entry.getValue());
      }
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

      shared.getDrops().replaceAll(event.getDrops());
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnPlayerShear trigger) {
          trigger.onPlayerShear(instance.getRank(), event.getPlayer(), event.getEntity(),
              event.getItem(), shared.getDrops());
        }
      }
      effectQueuePool.release(queue);
      event.setDrops(shared.getDrops());
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
    BlockState blockState = event.getBlockState();
    Location location = block.getLocation();
    if (!blocksDestroyed.contains(location)) {
      return;
    }
    blocksDestroyed.remove(location);
    Player player = event.getPlayer();
    try {
      EffectQueue<EffectInstance> queue = effectQueuePool.acquireAndFill(TriggerType.BLOCK_DROP,
          inventoryCache.get(player));
      if (queue.isEmpty()) {
        effectQueuePool.release(queue);
        return;
      }
      shared.getDrops().replaceAll(event.getItems().stream().map(Item::getItemStack));
      for (EffectInstance instance : queue) {
        if (instance.getEffect() instanceof IOnBlockDrop trigger) {
          trigger.onBlockDrop(instance.getRank(), player, block, blockState, shared.getDrops());
        }
      }
      effectQueuePool.release(queue);
      World world = location.getWorld();
      List<Item> items = shared.getDrops().stream().map(stack -> {
        Item item = world.createEntity(location, Item.class);
        item.setItemStack(stack);
        return item;
      }).toList();
      event.getItems().clear();
      event.getItems().addAll(items);
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
    PlayerInventory inventory = player.getInventory();
    ItemStack tool = inventory.getItemInMainHand();
    Block block = event.getBlock();
    EffectQueue<EffectInstance> queue;
    try {
      queue = effectQueuePool.acquireAndFill(TriggerType.BLOCK_BREAK,
          inventoryCache.get(player));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    shared.getExperience().set(event.getExpToDrop());
    for (EffectInstance instance : queue) {
      if (instance.getEffect() instanceof IOnBlockBreak trigger) {
        trigger.onBlockBreak(instance.getRank(), player, tool, lastClickedFace.get(player),
            block, shared.getExperience());
      }
    }
    effectQueuePool.release(queue);
    event.setExpToDrop(shared.getExperience().get());
    blocksDestroyed.add(block.getLocation());
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
