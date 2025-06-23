package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IGemItem.ISocketLimitCounterView;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ContainerCommand implements CommandExecutor {

  private final IRegistry<ISocketColor> colors;
  private final IGemItemFactory factory;

  @Inject
  public ContainerCommand(IRegistry<ISocketColor> colors, IGemItemFactory factory) {
    this.colors = colors;
    this.factory = factory;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      IGemItem item = factory.create(player.getInventory().getItemInMainHand());
      if (item == null) {
        return false;
      }
      ISocketLimitCounterView counter = item.getContainer()
          .getCounter(colors.get(SocketColors.BLUE));
      Bukkit.broadcastMessage(counter.getMax() + "");
      Bukkit.broadcastMessage(item.getContainer().toString());
    }
    return true;
  }
}
