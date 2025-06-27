package org.aincraft.effects;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.launchable.ILaunchable;
import org.aincraft.api.context.IShootBowContext;
import org.aincraft.api.trigger.IOnShootBow;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Material;
import org.bukkit.util.Vector;

final class Multishot extends AbstractGemEffect implements IOnShootBow {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.SHOOT_BOW, TargetType.RANGED_WEAPON)
    );
  }

  @Override
  public void onShootBow(IShootBowContext context, EffectInstanceMeta meta) {
    List<ILaunchable> instances = context.getLaunchables();
    if (instances.isEmpty()) {
      return;
    }
    double fanSpread = 20;
    int projectiles = meta.getRank() * Settings.MULTISHOT_PROJECTILES_RANK;
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
