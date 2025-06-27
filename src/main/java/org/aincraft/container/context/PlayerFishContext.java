package org.aincraft.container.context;

import org.aincraft.api.context.IPlayerFishContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class PlayerFishContext extends AbstractContext<PlayerFishEvent> implements
    IPlayerFishContext {

  PlayerFishContext(PlayerFishEvent event) {
    super(event);
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public FishHook getHook() {
    return event.getHook();
  }

  @Override
  public EquipmentSlot getHand() {
    return event.getHand();
  }

  @Override
  public void setExperience(int exp) {
    event.setExpToDrop(exp);
  }

  @Override
  public int getExperience() {
    return event.getExpToDrop();
  }

  @Override
  public void setDrops(ItemStack drops) {
    Item item = getItem();
    if (item != null) {
      item.setItemStack(drops);
    }
  }

  @Override
  @Nullable
  public ItemStack getDrops() {
    Item item = getItem();
    return item != null ? item.getItemStack() : null;
  }

  @Nullable
  private Item getItem() {
    Entity caught = event.getCaught();
    return caught instanceof Item item ? item : null;
  }
}
