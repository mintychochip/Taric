package org.aincraft.container.distribution;

import java.util.Map;
import org.bukkit.Material;

public interface IMiningDistributionConfiguration extends IDistributionConfiguration {

  int getMaxAmount();

  Map<Material, Double> getMaterialBaseChanceMap();

}
