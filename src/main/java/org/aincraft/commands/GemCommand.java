package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.container.rework.Gem;
import org.aincraft.container.rework.GemItem;
import org.aincraft.container.rework.IGem;
import org.aincraft.container.rework.IGem.IGemContainerView;
import org.aincraft.container.rework.SocketContainer;
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
      IGem gem = Gem.create(Material.EMERALD, colors.get(SocketColors.BLUE));
      GemItem
      IGemContainerView view = gem.getEffectContainer();
    }
    return false;
  }
}
