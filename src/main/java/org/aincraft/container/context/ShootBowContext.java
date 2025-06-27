package org.aincraft.container.context;

import java.util.ArrayList;
import java.util.List;
import org.aincraft.api.container.launchable.ILaunchable;
import org.aincraft.api.context.IShootBowContext;
import org.aincraft.container.launchable.LaunchableFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ShootBowContext extends AbstractContext<EntityShootBowEvent> implements
    IShootBowContext {

  private List<ILaunchable> launchables = new ArrayList<>();

  ShootBowContext(EntityShootBowEvent event) {
    super(event);
    launchables.add(LaunchableFactory.create((Projectile) event.getProjectile()));
  }

  @Override
  public LivingEntity getLauncher() {
    return event.getEntity();
  }

  @Override
  public List<ILaunchable> getLaunchables() {
    return launchables;
  }

  @Override
  public void setLaunchables(List<ILaunchable> launchables) {
    this.launchables = launchables;
  }
}
