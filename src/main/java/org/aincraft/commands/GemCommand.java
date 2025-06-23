package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.SocketColors;
import org.aincraft.api.container.gem.IPreciousGem;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IPreciousGemFactory factory;

  @Inject
  public GemCommand(
      IPreciousGemFactory factory) {
    this.factory = factory;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      for (ISocketColor value : SocketColors.values()) {
        IPreciousGem gem = factory.create(new ItemStack(Material.EMERALD));
        player.getInventory().addItem(gem.getStack());
      }

    }
    return false;
  }
}
