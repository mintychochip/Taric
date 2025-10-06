package org.aincraft.api.container;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface IEquipment {

  interface IEquipmentFactory {

    IEquipment create(Entity entity) throws IllegalArgumentException;
  }

  void setItem(EquipmentSlot slot, ItemStack item) throws IllegalArgumentException;

  ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException;

  ItemStack getItemInMainHand();

  void setItemInMainHand(ItemStack item);

  ItemStack getItemInOffHand();

  void setItemInOffHand(ItemStack item);

  ItemStack getHelmet();

  void setHelmet(ItemStack item);

  ItemStack getChestPlate();

  void setChestPlate(ItemStack item);

  ItemStack getLeggings();

  void setLeggings(ItemStack item);

  ItemStack getBoots();

  void setBoots(ItemStack item);

}
