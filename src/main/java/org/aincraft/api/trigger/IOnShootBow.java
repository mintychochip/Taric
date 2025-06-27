package org.aincraft.api.trigger;

import java.util.List;
import org.aincraft.api.container.launchable.ILaunchable;
import org.bukkit.entity.LivingEntity;

public interface IOnShootBow {

  void onShootBow(int rank, LivingEntity launcher, List<ILaunchable> instances);
}
