package org.aincraft.effects.launchable;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.equipment.EquipmentFactory;
import org.aincraft.api.effects.triggers.IOnShootBow.IArrowLaunchable;
import org.aincraft.api.effects.triggers.IOnShootBow.ILaunchable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

class ArrowLaunchable extends Launchable implements IArrowLaunchable {

  private boolean critical;
  private double damage;

  ArrowLaunchable(Vector velocity) {
    this(velocity, false, 1);
  }

  ArrowLaunchable(Arrow arrow) {
    this(arrow.getVelocity(), arrow.isCritical(), arrow.getDamage());
  }

  ArrowLaunchable(Vector velocity, boolean critical, double damage) {
    super(Arrow.class, velocity);
    this.critical = critical;
    this.damage = damage;
  }

  @Override
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(Arrow.class, velocity, a -> {
      IEquipment equipment = EquipmentFactory.equipmentFromEntity(shooter);
      ItemStack bow = equipment.getItemInMainHand();
      if (bow.getEnchantmentLevel(Enchantment.INFINITY) > 0) {
        a.setPickupStatus(PickupStatus.DISALLOWED);
      }
      a.setDamage(damage);
      a.setCritical(critical);
    });
  }

  @Override
  public void setCritical(boolean critical) {
    this.critical = critical;
  }

  @Override
  public void setDamage(double damage) {
    this.damage = damage;
  }

  @Override
  public boolean isCritical() {
    return critical;
  }

  @Override
  public double getDamage() {
    return damage;
  }

  @Override
  public ILaunchable clone() {
    return new ArrowLaunchable(velocity, critical, damage);
  }
}
