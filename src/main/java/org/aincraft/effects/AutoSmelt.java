package org.aincraft.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IOnBlockDrop;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.container.registerable.TriggerTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class AutoSmelt extends AbstractGemEffect implements IOnBlockDrop {

  private final Map<Material, ItemStack> conversions;

  private final AutoSmeltHelperBlockDrop helper;

  AutoSmelt(Map<Material, ItemStack> conversions) {
    this.conversions = conversions;
    this.helper = new AutoSmeltHelperBlockDrop(conversions);
  }

  public static @NotNull AutoSmelt create(@NotNull IConfiguration gemConfiguration) {
    Map<Material, ItemStack> conversions = new HashMap<>();
    String path = "auto-smelt.conversions";

    if (!gemConfiguration.contains(path)) {
      throw new IllegalStateException(
          "[AutoSmelt] Missing required configuration section: '" + path + "'. " +
              "Expected a list of block-to-material mappings like:\n" +
              "  cobblestone: stone\n  iron_ore: iron_ingot"
      );
    }

    ConfigurationSection conversionSection = gemConfiguration.getConfigurationSection(path);
    Set<String> sectionKeys = conversionSection.getKeys(false);

    for (String sectionKey : sectionKeys) {
      try {
        Material base = Material.valueOf(sectionKey.toUpperCase());
        String convertedRaw = conversionSection.getString(sectionKey, "").toUpperCase();
        Material converted = Material.valueOf(convertedRaw);
        conversions.put(base, new ItemStack(converted));
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().warning(
            "[AutoSmelt] Invalid material conversion in config: " +
                sectionKey + " → " + conversionSection.getString(sectionKey)
        );
      }
    }
    Taric.getLogger().info(
        String.format("[AutoSmelt] Loaded %d item conversions", conversions.size())
    );
    return new AutoSmelt(conversions);
  }

  private static final class AutoSmeltHelperBlockDrop extends BlockDropConversionHelper {

    private final Map<Material, ItemStack> conversions;

    private int maxRank;

    AutoSmeltHelperBlockDrop(Map<Material, ItemStack> conversions) {
      this.conversions = conversions;
    }

    @Override
    protected boolean conversionPredicate(IBlockDropContext context, EffectInstanceMeta meta,
        ItemStack stack) {
      int rank = meta.getRank();
      Material material = stack.getType();
      return conversions.containsKey(material) && rank >= maxRank
          || Taric.getRandom().nextInt(rank) != 0;
    }

    @Override
    protected ItemStack conversion(Material material) {
      return conversions.get(material);
    }

    //TODO fix later
    public void setMaxRank(int rank) {
      this.maxRank = rank;
    }
  }

  private static void playSmeltEffects(@NotNull Block block) {
    Location location = block.getLocation();
    World world = location.getWorld();
    if (Taric.getRandom().nextInt(3) == 0) {
      world.spawnParticle(Particle.LAVA,
          location.clone().add(0.5, 0.5, 0.5),
          1,
          0.1,
          0.1,
          0.1,
          null
      );
    }
  }

  @Override
  public String getName() {
    return "Auto-Smelt";
  }

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.BLOCK_BREAK, TargetType.TOOL)
    );
  }

  //  @Override
//  protected Map<TriggerType, Set<Material>> buildValidTargets() {
//    return Map.ofEntries(
//        Map.entry(TriggerType.BLOCK_DROP, TargetType.TOOL)
//    );
//  }

  @Override
  public void onBlockDrop(IBlockDropContext context, EffectInstanceMeta meta) {
    helper.setMaxRank(this.getMaxRank());
    helper.onBlockDrop(context, meta);
  }
}
