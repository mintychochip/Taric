package org.aincraft.listeners;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.aincraft.Settings;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.api.container.trigger.IOnBlockBreak;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.api.container.trigger.TriggerType;
import org.aincraft.container.trigger.BlockBreakContext;
import org.aincraft.container.trigger.BlockDropContext;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.EffectQueuePool.EffectQueue;
import org.aincraft.events.FakeBlockBreakEvent;
import org.aincraft.events.FakeBlockDropItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FakeEventListener implements Listener {

  private final LoadingCache<LivingEntity, IGemInventory> inventoryCache;
  private final EffectQueuePool<EffectInstance> queuePool;

  @Inject
  public FakeEventListener(LoadingCache<LivingEntity, IGemInventory> inventoryCache,
      EffectQueuePool<EffectInstance> queuePool) {
    this.inventoryCache = inventoryCache;
    this.queuePool = queuePool;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onFakeBlockBreak(final FakeBlockBreakEvent event) {
    Block block = event.getBlock();
    Material material = block.getType();
    if (block.isLiquid() || material.isAir()) {
      return;
    }
    Location location = block.getLocation();
    World world = location.getWorld();
    Player player = event.getPlayer();
    if (Settings.fakeEffectsShouldPlay(player)) {
      world.spawnParticle(Particle.BLOCK_CRUMBLE,
          location.clone().add(0.5, 0.5, 0.5),
          5,
          0.1, 0.1, 0.1,
          block.getBlockData()
      );
      world.playSound(
          location,
          block.getBlockSoundGroup().getBreakSound(),
          0.5f, 1.0f
      );
    }
    PlayerInventory inventory = player.getInventory();
    ItemStack tool = inventory.getItemInMainHand();
    if (material == Material.ICE && tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) == 0) {
      Location lowerLocation = location.clone().add(0, -1, 0);
      Block lowerBlock = lowerLocation.getBlock();
      if (lowerBlock.isSolid() || lowerBlock.isLiquid()) {
        block.setType(Material.WATER);
        return;
      }
    }
    BlockState state = block.getState();
    Location center = location.clone().add(0.5, 0.5, 0.5);
    List<Item> list = block.getDrops(player.getInventory().getItemInMainHand(), player).stream().map(stack -> {
      Item item = world.createEntity(center, Item.class);
      item.setItemStack(stack);
      return item;
    }).collect(Collectors.toList());
    block.setType(Material.AIR);
    if (event.getExpToDrop() > 0) {
      world.spawn(center, ExperienceOrb.class,
          orb -> orb.setExperience(event.getExpToDrop()));
    }
    Bukkit.getPluginManager().callEvent(new FakeBlockDropItemEvent(block,
        state, player,
        list));

  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onFakeBlockDrop(final FakeBlockBreakEvent event) {
    Player player = event.getPlayer();
    try {
      EffectQueue<EffectInstance> queue = queuePool.acquireAndFill(TriggerType.BLOCK_BREAK,
          inventoryCache.get(player));
      if (!queue.isEmpty()) {
        BlockBreakContext receiver = new BlockBreakContext();
        receiver.setHandle(event);
        receiver.setInitial(false);
        receiver.setBlockFace(null);
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnBlockBreak trigger) {
            receiver.setRank(instance.getRank());
            trigger.onBlockBreak(receiver);
          }
        }
      }
      queuePool.release(queue);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  private void onFakeBlockDropItem(final FakeBlockDropItemEvent event) {
    try {
      EffectQueue<EffectInstance> queue = queuePool.acquireAndFill(TriggerType.BLOCK_DROP,
          inventoryCache.get(
              event.getPlayer()));
      if (!queue.isEmpty()) {
        BlockDropContext receiver = new BlockDropContext();
        receiver.setHandle(event);
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnBlockDrop trigger) {
            receiver.setRank(instance.getRank());
            trigger.onBlockDrop(receiver);
          }
        }
      }
      queuePool.release(queue);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    for (Item item : event.getItems()) {
      Location location = item.getLocation();
      World world = location.getWorld();
      world.dropItem(location, item.getItemStack());
    }
  }
}