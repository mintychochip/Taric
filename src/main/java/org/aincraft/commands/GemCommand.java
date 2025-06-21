package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.container.rework.SocketContainer;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
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
      SocketContainer container = new SocketContainer();
      int count = container.getCount(colors.get(SocketColors.BLUE));
      Bukkit.broadcastMessage(count + " ");
      container.setCount(colors.get(SocketColors.BLUE),2);
      count = container.getCount(colors.get(SocketColors.BLUE));
      Bukkit.broadcastMessage(container.toString());
    }
    return false;
  }
}
