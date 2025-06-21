package org.aincraft.commands;

import com.google.inject.Inject;
import java.util.concurrent.ExecutionException;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.IGem;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.container.gem.Gem;
import org.aincraft.container.gem.GemItem;
import org.aincraft.effects.gems.Effects;
import org.aincraft.registry.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      IGemItem item = GemItem.create(new ItemStack(Material.BOW), 3);
      player.getInventory().addItem(item.getStack());
      IGem gem = Gem.create(Material.EMERALD);
      gem.editContainer(container -> {
        container.addEffect(Effects.SCAVENGE, 3);
        container.addEffect(Effects.INSIGHT,3);
        container.addEffect(Effects.HARVEST,3);
      });
      player.getInventory().addItem(gem.getStack());

    }
    return false;
  }
}
