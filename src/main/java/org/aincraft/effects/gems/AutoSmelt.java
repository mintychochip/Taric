package org.aincraft.effects.gems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Settings;
import org.aincraft.Taric;
import org.aincraft.config.IConfiguration;
import org.aincraft.container.TargetType;
import org.aincraft.effects.triggers.IOnBlockDrop;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class AutoSmelt extends AbstractGemEffect implements IOnBlockDrop {

  private final Map<Material, ItemStack> conversions = new HashMap<>();

  public static @NotNull AutoSmelt create(@NotNull IConfiguration gemConfiguration) {
    AutoSmelt as = new AutoSmelt();

    String path = "auto-smelt.conversions";

    if (!gemConfiguration.contains(path)) {
      throw new IllegalStateException(
          "[AutoSmelt] Missing required configuration section: '" + path + "'. " +
              "Expected a list of block-to-material mappings like:\n" +
              "  cobblestone: stone\n  iron_ore: iron_ingot"
      );
    }

    ConfigurationSection conversionSection = gemConfiguration.getSection(path);
    Set<String> sectionKeys = conversionSection.getKeys(false);

    for (String sectionKey : sectionKeys) {
      try {
        Material base = Material.valueOf(sectionKey.toUpperCase());
        String convertedRaw = conversionSection.getString(sectionKey, "").toUpperCase();
        Material converted = Material.valueOf(convertedRaw);
        as.addConversion(base, converted);
      } catch (IllegalArgumentException ex) {
        Taric.getLogger().warning(
            "[AutoSmelt] Invalid material conversion in config: " +
                sectionKey + " → " + conversionSection.getString(sectionKey)
        );
      }
    }
    Taric.getLogger().info(
        String.format("[AutoSmelt] Loaded %d item conversions", as.conversions.size())
    );

    return as;
  }

  public void addConversion(Material base, Material converted) {
    conversions.put(base, new ItemStack(converted, 1));
  }

  public void addConversion(Material base, ItemStack converted) {
    conversions.put(base, converted);
  }

  private boolean smelt(int level) {
    return level >= this.getMaxRank() || Taric.getRandom().nextInt(level + 1) != 0;
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
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.BLOCK_DROP, TargetType.TOOL)
    );
  }

  @Override
  public void onBlockDrop(int rank, Player player, Block block, BlockState blockState,
      List<ItemStack> drops) {
    if (drops.isEmpty()) {
      return;
    }
    List<ItemStack> modifiedDrops = new ArrayList<>(drops.size());
    for (ItemStack drop : drops) {
      Material type = drop.getType();
      if (smelt(rank) && conversions.containsKey(type)) {
        ItemStack smelted = conversions.get(type);
        if (smelted != null) {
          ItemStack newSmelted = smelted.clone();
          newSmelted.setAmount(drop.getAmount());
          modifiedDrops.add(newSmelted);
        } else {
          modifiedDrops.add(drop.clone());
        }
        if (Settings.fakeEffectsShouldPlay(player)) {
          playSmeltEffects(blockState.getBlock());
        }
      } else {
        modifiedDrops.add(drop.clone());
      }
    }
    drops.clear();
    drops.addAll(modifiedDrops);
  }
}
