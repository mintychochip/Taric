package org.aincraft.api.container;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface IEquipment {

  void setItem(EquipmentSlot slot, ItemStack item) throws IllegalArgumentException;

  ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException;

  void setItemInMainHand(ItemStack item);

  ItemStack getItemInMainHand();

  void setItemInOffHand(ItemStack item);

  ItemStack getItemInOffHand();

  void setHelmet(ItemStack item);

  ItemStack getHelmet();

  void setChestPlate(ItemStack item);

  ItemStack getChestPlate();

  void setLeggings(ItemStack item);

  ItemStack getLeggings();

  void setBoots(ItemStack item);

  ItemStack getBoots();

  interface IEquipmentFactory {

    IEquipment create(Entity entity) throws IllegalArgumentException;
  }

}
