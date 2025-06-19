package org.aincraft.effects.launchable;

import org.aincraft.api.container.IEquipment;
import org.aincraft.api.effects.triggers.IOnShootBow.IArrowLaunchable;
import org.aincraft.api.effects.triggers.IOnShootBow.IFireworkLaunchable;
import org.aincraft.api.effects.triggers.IOnShootBow.ILaunchable;
import org.aincraft.container.equipment.EquipmentFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

class Launchable implements ILaunchable {

  protected final Class<? extends Projectile> projectileClazz;
  protected Vector velocity;

  Launchable(Class<? extends Projectile> projectileClazz, Vector velocity) {
    this.projectileClazz = projectileClazz;
    this.velocity = velocity;
  }

  @Override
  public void launch(LivingEntity shooter) {
    shooter.launchProjectile(projectileClazz, velocity);
  }

  @Override
  public Vector getVelocity() {
    return velocity;
  }

  @Override
  public void setVelocity(Vector velocity) {
    this.velocity = velocity;
  }

  public Class<? extends Projectile> getProjectileClazz() {
    return projectileClazz;
  }

  @Override
  public ILaunchable clone() {
    try {
      return (Launchable) super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }
}
