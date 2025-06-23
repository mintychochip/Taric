package org.aincraft.listeners;

import org.aincraft.container.trigger.BlockDropContext;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;

public class GeodeListener implements Listener {
  private static final Material[] APPLICABLE = new Material[]{Material.EMERALD_ORE, Material.DIAMOND_ORE};

  @EventHandler(priority = EventPriority.HIGH)
  public void onBreakListener(final BlockDropItemEvent event) {
    BlockState blockState = event.getBlockState();
    BlockDropContext context = new BlockDropContext();
    context.setHandle(event);
  }
}
