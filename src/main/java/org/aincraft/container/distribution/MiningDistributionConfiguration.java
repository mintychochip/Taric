package org.aincraft.container.distribution;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.config.IConfiguration;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

final class MiningDistributionConfiguration extends AbstractDistributionConfiguration implements
    IMiningDistributionConfiguration {

  private final Map<Material, Double> materialBaseChance;

  MiningDistributionConfiguration(boolean enabled, int maxAmount,
      Map<Material, Double> materialBaseChance) {
    super(enabled, maxAmount, new Permission("taric.distribution.mining", PermissionDefault.OP));
    this.materialBaseChance = materialBaseChance;
  }

  static final class MiningConfigurationProvider implements
      Provider<IMiningDistributionConfiguration> {

    private final IConfiguration configuration;

    @Inject
    MiningConfigurationProvider(@Named("distribution") IConfiguration configuration) {
      this.configuration = configuration;
    }

    @Override
    public IMiningDistributionConfiguration get() {
      try {
        Preconditions.checkArgument(configuration.contains("mining"));
        ConfigurationSection miningSection = configuration.getConfigurationSection("mining");
        Preconditions.checkArgument(miningSection.contains("max-amount"));
        Preconditions.checkArgument(miningSection.contains("material-map"));
        Preconditions.checkArgument(miningSection.contains("enabled"));
        int maxAmount = miningSection.getInt("max-amount");
        ConfigurationSection materialMapSection = miningSection.getConfigurationSection(
            "material-map");
        if (materialMapSection == null) {
          throw new IllegalArgumentException("");
        }
        Map<Material, Double> materialBaseChance = new HashMap<>();
        for (String key : materialMapSection.getKeys(false)) {
          double baseChance = materialMapSection.getDouble(key);
          Material material = Material.valueOf(key);
          materialBaseChance.put(material, baseChance);
        }
        boolean enabled = miningSection.getBoolean("enabled");
        return new MiningDistributionConfiguration(enabled, maxAmount, materialBaseChance);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public Map<Material, Double> getMaterialBaseChanceMap() {
    return materialBaseChance;
  }
}
