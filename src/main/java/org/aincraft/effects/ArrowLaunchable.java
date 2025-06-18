package org.aincraft.effects;

import org.aincraft.api.container.IEquipment;
import org.aincraft.container.equipment.EquipmentFactory;
import org.aincraft.effects.triggers.IOnShootBow.IArrowLaunchable;
import org.aincraft.effects.triggers.IOnShootBow.ILaunchable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ArrowLaunchable extends AbstractLaunchable implements IArrowLaunchable {

  private boolean critical;
  private double damage;

  public ArrowLaunchable(Arrow arrow) {
    this(arrow.getVelocity(), arrow.isCritical(), arrow.getDamage());
  }

  public ArrowLaunchable(Vector velocity, boolean critical, double damage) {
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
