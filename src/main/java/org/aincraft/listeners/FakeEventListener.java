package org.aincraft.listeners;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.aincraft.Settings;
import org.aincraft.api.container.gem.IGemInventory;
import org.aincraft.container.Mutable;
import org.aincraft.effects.EffectQueuePool;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.EffectQueuePool.EffectQueue;
import org.aincraft.effects.triggers.IOnBlockDrop;
import org.aincraft.effects.triggers.TriggerType;
import org.aincraft.events.FakeBlockBreakEvent;
import org.aincraft.events.FakeBlockDropItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
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

  private static final List<ItemStack> FAKE_DROP_LIST = new ArrayList<>();
  private static final Mutable<Integer> EXPERIENCE_MUTABLE = new Mutable<>(0);
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
    block.setType(Material.AIR);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onFakeBlockDrop(final FakeBlockBreakEvent event) {
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();
    ItemStack tool = inventory.getItemInMainHand();
    Block block = event.getBlock();
    try {
      EffectQueue<EffectInstance> queue = queuePool.acquireAndFill(TriggerType.BLOCK_DROP,
          inventoryCache.get(player));
      FAKE_DROP_LIST.clear();
      FAKE_DROP_LIST.addAll(block.getDrops(tool, player));
      EXPERIENCE_MUTABLE.set(event.getExpToDrop());
      if (!queue.isEmpty()) {
        for (EffectInstance instance : queue) {
          if (instance.getEffect() instanceof IOnBlockDrop trigger) {
            trigger.onBlockDrop(instance.getRank(), player, block, block.getState(), FAKE_DROP_LIST);
          }
        }
      }
      queuePool.release(queue);
      Location location = block.getLocation();
      Location center = location.clone().add(0.5, 0.5, 0.5);
      World world = location.getWorld();
      List<Item> list = FAKE_DROP_LIST.stream().map(stack -> {
        Item item = world.createEntity(center, Item.class);
        item.setItemStack(stack);
        return item;
      }).toList();
      if (EXPERIENCE_MUTABLE.get() > 0) {
        world.spawn(location, ExperienceOrb.class, orb -> orb.setExperience(EXPERIENCE_MUTABLE.get()));
      }
      Bukkit.getPluginManager().callEvent(new FakeBlockDropItemEvent(block,
          block.getState(), player,
          list));
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  private void onFakeBlockDropItem(final FakeBlockDropItemEvent event) {
    for (Item item : event.getItems()) {
      Location location = item.getLocation();
      World world = location.getWorld();
      world.dropItem(location, item.getItemStack());
    }
  }
}
