package org.aincraft.effects.gems;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.container.TargetType;
import org.aincraft.effects.triggers.IOnShootBow;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

final class Multishot extends AbstractGemEffect implements IOnShootBow {

  Multishot(String key) {
    super(key);
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.SHOOT_BOW, TargetType.RANGED_WEAPON)
    );
  }

  @Override
  public void onShootBow(int rank, LivingEntity launcher, List<ILaunchable> instances) {
    if (instances.isEmpty()) {
      return;
    }
    double fanSpread = 20;
    int projectiles = rank * Settings.MULTISHOT_PROJECTILES_RANK;
    if (projectiles <= 1) {
      return;
    }
    double step = fanSpread / projectiles;
    ILaunchable launchable = instances.get(0);
    for (int i = 1; i <= projectiles / 2; ++i) {
      double angle = i * step;
      Vector velocity = launchable.getVelocity();
      ILaunchable right = launchable.clone();
      right.setVelocity(velocity.clone().rotateAroundY(Math.toRadians(angle)));
      instances.add(right);
      ILaunchable left = launchable.clone();
      left.setVelocity(velocity.clone().rotateAroundY(Math.toRadians(-angle)));
      instances.add(left);
    }
  }
}
