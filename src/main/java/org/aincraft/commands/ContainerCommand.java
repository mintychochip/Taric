package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.container.rework.GemItem;
import org.aincraft.container.rework.IGemItem;
import org.aincraft.container.rework.IGemItem.ISocketLimitCounterView;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerCommand implements CommandExecutor {

  private final IRegistry<ISocketColor> colors;

  @Inject
  public ContainerCommand(IRegistry<ISocketColor> colors) {
    this.colors = colors;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ItemStack mainHand = player.getInventory().getItemInMainHand();
      IGemItem item = GemItem.fromIfExists(mainHand);
      if (item == null) {
        return false;
      }
      ISocketLimitCounterView counter = item.getEffectContainer()
          .getCounter(colors.get(SocketColors.BLUE));
      Bukkit.broadcastMessage(counter.getMax() + "");
      Bukkit.broadcastMessage(item.getEffectContainer().toString());
    }
    return true;
  }
}
