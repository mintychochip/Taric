package org.aincraft.api.container;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface Equipment {

  void setItem(EquipmentSlot slot, ItemStack item) throws IllegalArgumentException;

  ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException;

  ItemStack getItemInMainHand();

  void setItemInMainHand(ItemStack item);

  ItemStack getItemInOffHand();

  void setItemInOffHand(ItemStack item);

  ItemStack getHelmet();

  void setHelmet(ItemStack item);

  ItemStack getChestplate();

  void setChestPlate(ItemStack item);

  ItemStack getLeggings();

  void setLeggings(ItemStack item);

  ItemStack getBoots();

  void setBoots(ItemStack item);

}
