package org.aincraft.container.launchable;

import org.aincraft.api.container.launchable.IArrowLaunchable;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

class ArrowLaunchable extends Launchable implements IArrowLaunchable {

  private boolean critical;
  private double damage;

  ArrowLaunchable(Arrow arrow) {
    this(arrow.getVelocity(), arrow.getLocation(), arrow.isCritical(), arrow.getDamage());
  }

  ArrowLaunchable(Vector velocity, Location location, boolean critical, double damage) {
    super(Arrow.class, velocity, location);
    this.critical = critical;
    this.damage = damage;
  }

  @Override
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(Arrow.class, velocity, a -> {
      EntityEquipment entityEquipment = shooter.getEquipment();
      if (entityEquipment != null) {
        ItemStack bow = entityEquipment.getItemInMainHand();
        if (bow.getEnchantmentLevel(Enchantment.INFINITY) > 0) {
          a.setPickupStatus(PickupStatus.DISALLOWED);
        }
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
    return new ArrowLaunchable(velocity, location, critical, damage);
  }
}
