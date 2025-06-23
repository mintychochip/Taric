package org.aincraft.container;

import org.aincraft.api.container.IEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

class EntityEquipment extends ForwardingEquipment {

  private final org.bukkit.inventory.EntityEquipment entityEquipment;

  private final IEquipment delegate;

  public EntityEquipment(org.bukkit.inventory.EntityEquipment entityEquipment) {
    this.entityEquipment = entityEquipment;
    this.delegate = new MobEquipmentDecorator(entityEquipment);
  }

  @Override
  protected IEquipment delegate() {
    return delegate;
  }

  public org.bukkit.inventory.EntityEquipment getEntityEquipment() {
    return entityEquipment;
  }

  public static final class MobEquipmentDecorator implements IEquipment {

    private final org.bukkit.inventory.EntityEquipment entityEquipment;

    private MobEquipmentDecorator(org.bukkit.inventory.EntityEquipment entityEquipment) {
      this.entityEquipment = entityEquipment;
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
      entityEquipment.setItem(slot, item);
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) throws IllegalArgumentException {
      return entityEquipment.getItem(slot);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
      entityEquipment.setItemInMainHand(item);
    }

    @Override
    public ItemStack getItemInMainHand() {
      return entityEquipment.getItemInMainHand();
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
      entityEquipment.setItemInOffHand(item);
    }

    @Override
    public ItemStack getItemInOffHand() {
      return entityEquipment.getItemInOffHand();
    }

    @Override
    public void setHelmet(ItemStack item) {
      entityEquipment.setHelmet(item);
    }

    @Override
    public ItemStack getHelmet() {
      return entityEquipment.getHelmet();
    }

    @Override
    public void setChestPlate(ItemStack item) {
      entityEquipment.setChestplate(item);
    }

    @Override
    public ItemStack getChestPlate() {
      return entityEquipment.getChestplate();
    }

    @Override
    public void setLeggings(ItemStack item) {
      entityEquipment.setLeggings(item);
    }

    @Override
    public ItemStack getLeggings() {
      return entityEquipment.getLeggings();
    }

    @Override
    public void setBoots(ItemStack item) {
      entityEquipment.setBoots(item);
    }

    @Override
    public ItemStack getBoots() {
      return entityEquipment.getBoots();
    }
  }

}
