package org.aincraft.commands;

import com.google.inject.Inject;
import java.util.concurrent.ExecutionException;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.container.gem.GemItem;
import org.aincraft.effects.gems.Effects;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      try {
        IGemItem item = GemItem.from(player.getInventory().getItemInMainHand(),
            () -> GemItem.create(player.getInventory().getItemInMainHand(), 3));
        item.editContainer(container -> {
          container.addEffect(Effects.VEIN_MINER,3,true);
//          container.addEffect(Effects.HARVEST,3);
          container.addEffect(Effects.INSIGHT,3);
        });
      } catch (ExecutionException | IllegalArgumentException e) {
        throw new RuntimeException(e);
      }
    }
    return false;
  }
}
