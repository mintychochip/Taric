package org.aincraft.container.context;

import org.aincraft.api.trigger.IOnInteract.IInteractContext;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class InteractContext extends AbstractContext<PlayerInteractEvent> implements
    IInteractContext {

  InteractContext(PlayerInteractEvent event) {
    super(event);
  }

  @Override
  public @NotNull Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public @Nullable ItemStack getItem() {
    return event.getItem();
  }

  @Override
  public @NotNull Action getAction() {
    return event.getAction();
  }

  @Override
  public @Nullable Block getBlock() {
    return event.getClickedBlock();
  }

  @Override
  public @NotNull BlockFace getBlockFace() {
    return event.getBlockFace();
  }

  @Override
  public @Nullable Location getLocation() {
    return event.getInteractionPoint();
  }

  @Override
  public EquipmentSlot getHand() {
    return event.getHand();
  }

  @Override
  public @NotNull Result useItemInHand() {
    return event.useItemInHand();
  }

  @Override
  public void setUseInteractedBlock(@NotNull Result use) {
    event.setUseInteractedBlock(use);
  }

  @Override
  public void setUseItemInHand(@NotNull Result use) {
    event.setUseItemInHand(use);
  }

  @Override
  public boolean hasItem() {
    return event.hasItem();
  }

  @Override
  public boolean hasBlock() {
    return event.hasBlock();
  }

  @Override
  public boolean isBlockInHand() {
    return event.isBlockInHand();
  }
}
