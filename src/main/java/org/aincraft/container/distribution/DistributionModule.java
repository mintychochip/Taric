package org.aincraft.container.distribution;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.aincraft.container.distribution.ChestDistributionConfiguration.ChestConfigurationProvider;
import org.aincraft.container.distribution.MiningDistributionConfiguration.MiningConfigurationProvider;

public final class DistributionModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(IChestDistributionConfiguration.class).toProvider(ChestConfigurationProvider.class)
        .in(Singleton.class);
    bind(IMiningDistributionConfiguration.class).toProvider(MiningConfigurationProvider.class)
        .in(Singleton.class);
  }
}
