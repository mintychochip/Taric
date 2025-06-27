package org.aincraft.api.context;

import java.util.List;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.entity.LivingEntity;

public interface IShootBowContext {

  LivingEntity getLauncher();

  List<ILaunchable> getLaunchables();

  void setLaunchables(List<ILaunchable> launchables);
}
