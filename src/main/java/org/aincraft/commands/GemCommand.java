package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.container.rework.GemItem;
import org.aincraft.container.rework.IGemItem;
import org.aincraft.container.rework.IGemItem.ISocketLimitCounterView;
import org.aincraft.effects.gems.Effects;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IRegistry<ISocketColor> colors;

  @Inject
  public GemCommand(IRegistry<ISocketColor> colors) {
    this.colors = colors;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {


      IGemItem item = GemItem.create(Material.DIAMOND_PICKAXE);
      item.editContainer(container -> {
        container.initializeCounter(colors.get(SocketColors.BLUE),3);
        container.initializeCounter(colors.get(SocketColors.RED),1);
        container.initializeCounter(colors.get(SocketColors.YELLOW),2);
        container.setEffect(Effects.VEIN_MINER,3);
      });
      ISocketLimitCounterView counter = item.getEffectContainer()
          .getCounter(colors.get(SocketColors.BLUE));
      Bukkit.broadcastMessage(counter.getMax() + "");
      player.getInventory().addItem(item.getStack());
    }
    return false;
  }
}
