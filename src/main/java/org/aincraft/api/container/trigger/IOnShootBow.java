package org.aincraft.api.container.trigger;

import java.util.List;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.entity.LivingEntity;

public interface IOnShootBow {

  interface IShootBowContext {

    LivingEntity getLauncher();

    List<ILaunchable> getLaunchables();

    void setLaunchables(List<ILaunchable> launchables);
  }

  void onShootBow(int rank, LivingEntity launcher, List<ILaunchable> instances);
}
