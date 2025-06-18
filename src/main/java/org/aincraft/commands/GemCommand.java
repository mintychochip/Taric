package org.aincraft.commands;

import java.util.concurrent.ExecutionException;
import org.aincraft.Taric;
import org.aincraft.container.gem.exceptions.CapacityException;
import org.aincraft.container.gem.GemItem;
import org.aincraft.container.gem.exceptions.GemConflictException;
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
//        Gem gem = Gem.from(player.getInventory().getItemInMainHand(),
//            () -> Gem.create(player.getInventory().getItemInMainHand()));
//        gem.editContainer(container -> {
//          container.addEffect(GemEffects.BURROWING,3);
//          container.addEffect(GemEffects.AUTO_SMELT, 15);
//          container.addEffect(GemEffects.BLINK, 3);
//          container.removeEffect(GemEffects.COLD_ASPECT);
//          Bukkit.broadcastMessage(container.toString());
//        });
        GemItem item = GemItem.from(player.getInventory().getItemInMainHand(),
            () -> GemItem.create(player.getInventory().getItemInMainHand(),3));
        item.editContainer(container -> {
          try {
//            container.addEffect(GemEffects.FLARE,3);
            container.addEffect(GemEffects.COLD_ASPECT,3);
            container.addEffect(GemEffects.VAMPIRISM,3);
          } catch (CapacityException e) {
            Taric.getLogger().info(e.getMessage());
          }
        });
      } catch (ExecutionException | IllegalArgumentException e) {
        throw new RuntimeException(e);
      }
    }
    return false;
  }
}
