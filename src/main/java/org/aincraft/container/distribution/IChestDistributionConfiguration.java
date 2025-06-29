package org.aincraft.container.distribution;

import java.util.List;
import org.bukkit.NamespacedKey;

public interface IChestDistributionConfiguration extends IDistributionConfiguration {

  double getBaseChance();

  boolean isLuckApplicable();

  List<NamespacedKey> getLootTableWhitelist();

  int getMaxAmount();

}
