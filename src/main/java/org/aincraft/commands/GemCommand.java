package org.aincraft.commands;

import java.util.concurrent.ExecutionException;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.container.gem.GemItem;
import org.aincraft.effects.gems.GemEffects;
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
          container.addEffect(GemEffects.MULTISHOT,15, true);
          container.addEffect(GemEffects.FLARE, 3);
//          container.addEffect(GemEffects.BURROWING, 3, true);
//          container.addEffect(GemEffects.FLARE, 3, true);
        });
      } catch (ExecutionException | IllegalArgumentException e) {
        throw new RuntimeException(e);
      }
    }
    return false;
  }
}
