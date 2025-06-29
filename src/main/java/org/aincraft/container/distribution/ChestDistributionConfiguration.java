package org.aincraft.container.distribution;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.List;
import org.aincraft.api.config.IConfiguration;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

final class ChestDistributionConfiguration extends AbstractDistributionConfiguration implements
    IChestDistributionConfiguration {

  private final List<NamespacedKey> lootTableWhitelist;

  private final boolean luckApplicable;

  private final double baseChance;

  ChestDistributionConfiguration(boolean enabled, int maxAmount, double baseChance,
      List<NamespacedKey> lootTableWhitelist,
      boolean luckApplicable) {
    super(enabled, maxAmount, new Permission("taric.distribution.chest", PermissionDefault.OP));
    this.baseChance = baseChance;
    this.lootTableWhitelist = lootTableWhitelist;
    this.luckApplicable = luckApplicable;
  }

  static final class ChestConfigurationProvider implements
      Provider<IChestDistributionConfiguration> {

    private final IConfiguration configuration;

    @Inject
    ChestConfigurationProvider(@Named("distribution") IConfiguration configuration) {
      this.configuration = configuration;
    }

    @Override
    public IChestDistributionConfiguration get() {
      try {
        Preconditions.checkArgument(configuration.contains("chest"));
        ConfigurationSection chestSection = configuration.getConfigurationSection("chest");
        Preconditions.checkArgument(chestSection.contains("max-amount"));
        Preconditions.checkArgument(chestSection.contains("enabled"));
        Preconditions.checkArgument(chestSection.contains("base"));
        Preconditions.checkArgument(chestSection.contains("luck-applicable"));
        Preconditions.checkArgument(chestSection.contains("loot-table-whitelist"));
        int maxAmount = chestSection.getInt("max-amount");
        double baseChance = chestSection.getDouble("base");
        boolean enabled = chestSection.getBoolean("enabled");
        boolean luckApplicable = chestSection.getBoolean("luck-applicable");
        List<NamespacedKey> lootTableWhitelist = chestSection.getStringList("loot-table-whitelist")
            .stream()
            .map(str -> {
              String[] split = str.split(":");
              return new NamespacedKey(split[0], split[1]);
            }).toList();
        return new ChestDistributionConfiguration(enabled, maxAmount, baseChance,
            lootTableWhitelist,
            luckApplicable);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public double getBaseChance() {
    return baseChance;
  }

  @Override
  public boolean isLuckApplicable() {
    return luckApplicable;
  }

  @Override
  public List<NamespacedKey> getLootTableWhitelist() {
    return lootTableWhitelist;
  }
}
