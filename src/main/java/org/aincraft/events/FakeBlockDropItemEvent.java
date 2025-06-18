package org.aincraft.events;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class FakeBlockDropItemEvent extends BlockDropItemEvent {

  public FakeBlockDropItemEvent(@NotNull Block block,
      @NotNull BlockState blockState,
      @NotNull Player player,
      @NotNull List<Item> items) {
    super(block, blockState, player, items);
  }
}
