package org.aincraft.commands;

import org.aincraft.container.GemItem;
import org.aincraft.effects.gems.GemEffects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class ToItem implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      PlayerInventory inventory = player.getInventory();
      ItemStack item = inventory.getItemInMainHand();
      if (!GemItem.is(item)) {
        GemItem gemItem = GemItem.create(item, 1);
        gemItem.setContents(contents -> contents
            .setEffect(GemEffects.VEIN_MINER,3)
            .setEffect(GemEffects.BURROWING,3)
            .setEffect(GemEffects.AUTO_SMELT,3)
//            .setEffect(GemEffects.MIND_CONTROL,3)
            .setEffect(GemEffects.VORPAL,3)
            .setEffect(GemEffects.COLD_ASPECT,3)
            .setEffect(GemEffects.MULTISHOT, 3)
            .setEffect(GemEffects.PRISMATIC,3)
            .setEffect(GemEffects.FLARE,3)
            .setEffect(GemEffects.BLINK,3)
            .setEffect(GemEffects.SCAVENGE,3));
      }
    }
    return false;
  }
}
