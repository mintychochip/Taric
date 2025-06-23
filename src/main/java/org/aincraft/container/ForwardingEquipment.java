package org.aincraft.container;

import org.aincraft.api.container.IEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

abstract class ForwardingEquipment implements IEquipment {

  protected abstract IEquipment delegate();

  @Override
  public void setItem(EquipmentSlot slot, ItemStack item) throws IllegalArgumentException {
    delegate().setItem(slot, item);
  }

  @Override
  public ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException {
    return delegate().getItem(slot);
  }

  @Override
  public void setItemInMainHand(ItemStack item) {
    delegate().setItemInMainHand(item);
  }

  @Override
  public ItemStack getItemInMainHand() {
    return delegate().getItemInMainHand();
  }

  @Override
  public void setItemInOffHand(ItemStack item) {
    delegate().setItemInOffHand(item);
  }

  @Override
  public ItemStack getItemInOffHand() {
    return delegate().getItemInOffHand();
  }

  @Override
  public void setHelmet(ItemStack item) {
    delegate().setHelmet(item);
  }

  @Override
  public ItemStack getHelmet() {
    return delegate().getHelmet();
  }

  @Override
  public void setChestPlate(ItemStack item) {
    delegate().setChestPlate(item);
  }

  @Override
  public ItemStack getChestPlate() {
    return delegate().getChestPlate();
  }

  @Override
  public void setLeggings(ItemStack item) {
    delegate().setLeggings(item);
  }

  @Override
  public ItemStack getLeggings() {
    return delegate().getLeggings();
  }

  @Override
  public void setBoots(ItemStack item) {
    delegate().setBoots(item);
  }

  @Override
  public ItemStack getBoots() {
    return delegate().getBoots();
  }
}
