package org.aincraft.listeners;

import com.google.inject.Inject;
import org.aincraft.api.container.gem.AppraisalState;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IPreciousGem;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemContainerView;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GeodeListener implements Listener {

  private final IGemIdentifier identifier;
  private final IPreciousGemFactory preciousGemFactory;
  private final IGemItemFactory gemItemFactory;
  private final ISocketGemFactory socketGemFactory;

  @Inject
  public GeodeListener(IGemIdentifier identifier, IPreciousGemFactory preciousGemFactory,
      IGemItemFactory gemItemFactory, ISocketGemFactory socketGemFactory) {
    this.identifier = identifier;
    this.preciousGemFactory = preciousGemFactory;
    this.gemItemFactory = gemItemFactory;
    this.socketGemFactory = socketGemFactory;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onAppraiseGem(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null || block.getType().isAir()) {
      return;
    }

    Action action = event.getAction();
    if (action.isLeftClick() || action == Action.PHYSICAL) {
      return;
    }

    ItemStack item = event.getItem();
    if (item == null) {
      return;
    }

    IPreciousGem gem = preciousGemFactory.fromIfExists(item);
    if (gem == null) {
      return;
    }

    Player player = event.getPlayer();
    IPreciousGemContainerView view = gem.getContainer();
    AppraisalState state = view.getState();
    EquipmentSlot hand = event.getHand();
    assert hand != null;
    if (state != AppraisalState.INITIAL && state != AppraisalState.GROUND) {
      return;
    }
    Material type = block.getType();

    if (state == AppraisalState.INITIAL && type == Material.GRINDSTONE) {
      event.setCancelled(true);
      gem.editContainer(container -> container.setState(AppraisalState.GROUND));
      player.playSound(player, Sound.BLOCK_GRINDSTONE_USE, 1.0f, 1.0f);
    }

    if (state == AppraisalState.GROUND && type == Material.WATER_CAULDRON
        && block.getBlockData() instanceof Levelled levelled) {
      event.setCancelled(true);
      int level = levelled.getLevel() - 1;
      if (level == 0) {
        block.setType(Material.CAULDRON);
      } else {
        levelled.setLevel(level);
        block.setBlockData(levelled);
      }
      ISocketGem newGem = identifier.identify(gem, player);
      PlayerInventory inventory = player.getInventory();
      inventory.setItem(hand, newGem.getStack());
      player.playSound(player, Sound.ITEM_BUCKET_FILL, 1.0f, 1.0f);
    }
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  private void onClick(final InventoryClickEvent event) {
    if (event.getAction() != InventoryAction.SWAP_WITH_CURSOR) {
      return;
    }

    final ItemStack cursor = event.getCursor();
    final ItemStack current = event.getCurrentItem();

    final ISocketGem socketGem = socketGemFactory.fromIfExists(cursor);
    final ISocketGem otherGem = socketGemFactory.fromIfExists(current);
    final IGemItem gemItem = gemItemFactory.fromIfExists(current);

    final Player player = (Player) event.getWhoClicked();

    if (socketGem != null && gemItem != null) {

      try {
        socketGem.editContainer(container -> container.move(gemItem));
      } catch (IllegalArgumentException | NullPointerException | IllegalStateException ex) {
        player.sendMessage(ex.getMessage());
        return;
      }

      final ISocketGemContainerView view = socketGem.getContainer();
      final IGemEffect effect = view.getEffect();

      if (effect == null) {
        player.setItemOnCursor(null);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
      }
      event.setCancelled(true);
    }
    if (socketGem != null && otherGem != null) {
      try {
        otherGem.editContainer(container -> {
          container.merge(socketGem);
        });
      } catch (IllegalStateException | NullPointerException | IllegalArgumentException ex) {
        player.sendMessage(ex.getMessage());
        return;
      }

      final ISocketGemContainerView view = socketGem.getContainer();
      final IGemEffect effect = view.getEffect();

      if (effect == null) {
        player.setItemOnCursor(null);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
      }
      event.setCancelled(true);
    }
  }
}
