package org.aincraft.container.distribution;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemFactory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;

public class DistributionListener implements Listener {

  private final IUnidentifiedGemFactory preciousGemFactory;

  private final IGemItemFactory gemItemFactory;

  private final IConfiguration distributionConfig;

  @Inject
  public DistributionListener(IUnidentifiedGemFactory preciousGemFactory,
      IGemItemFactory gemItemFactory, @Named("distribution") IConfiguration distributionConfig) {
    this.preciousGemFactory = preciousGemFactory;
    this.gemItemFactory = gemItemFactory;
    this.distributionConfig = distributionConfig;
  }

  @EventHandler
  public void onChestOpen(final LootGenerateEvent event) {
    List<String> whitelist = distributionConfig.getStringList("loot-chest.loot-table-whitelist");
    Bukkit.broadcast(Component.text(whitelist.toString()));
  }


}
