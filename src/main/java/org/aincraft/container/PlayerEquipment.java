package org.aincraft.container;

import org.aincraft.api.container.IEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

final class PlayerEquipment extends ForwardingEquipment {

  private final PlayerInventory playerInventory;
  private final IEquipment delegate;

  public PlayerEquipment(PlayerInventory playerInventory) {
    this.playerInventory = playerInventory;
    this.delegate = new PlayerInventoryDecorator(playerInventory);
  }

  @Override
  protected IEquipment delegate() {
    return delegate;
  }

  public PlayerInventory getPlayerInventory() {
    return playerInventory;
  }

  private record PlayerInventoryDecorator(PlayerInventory playerInventory) implements IEquipment {

    @Override
      public void setItem(EquipmentSlot slot, ItemStack item) {
        playerInventory.setItem(slot, item);
      }

      @Override
      public ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException {
        return playerInventory.getItem(slot);
      }

      @Override
      public void setItemInMainHand(ItemStack item) {
        playerInventory.setItemInMainHand(item);
      }

      @Override
      public ItemStack getItemInMainHand() {
        return playerInventory.getItemInMainHand();
      }

      @Override
      public void setItemInOffHand(ItemStack item) {
        playerInventory.setItemInOffHand(item);
      }

      @Override
      public ItemStack getItemInOffHand() {
        return playerInventory.getItemInOffHand();
      }

      @Override
      public void setHelmet(ItemStack item) {
        playerInventory.setHelmet(item);
      }

      @Override
      public ItemStack getHelmet() {
        return playerInventory.getHelmet();
      }

      @Override
      public void setChestPlate(ItemStack item) {
        playerInventory.setChestplate(item);
      }

      @Override
      public ItemStack getChestPlate() {
        return playerInventory.getChestplate();
      }

      @Override
      public void setLeggings(ItemStack item) {
        playerInventory.setLeggings(item);
      }

      @Override
      public ItemStack getLeggings() {
        return playerInventory.getLeggings();
      }

      @Override
      public void setBoots(ItemStack item) {
        playerInventory.setBoots(item);
      }

      @Override
      public ItemStack getBoots() {
        return playerInventory.getBoots();
      }
    }

}
