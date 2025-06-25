package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.SocketColors;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.effects.gems.Effects;
import org.aincraft.registry.IRegistry;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IPreciousGemFactory factory;

  private final IGemItemFactory itemFactory;
  private final ISocketGemFactory gemFactory;
  private final IRegistry<IIdentificationTable> tableRegistry;
  private final IGemIdentifier identifier;

  @Inject
  public GemCommand(
      IPreciousGemFactory factory, IGemItemFactory itemFactory, ISocketGemFactory gemFactory,
      IRegistry<IIdentificationTable> tableRegistry,
      IGemIdentifier identifier) {
    this.factory = factory;
    this.itemFactory = itemFactory;
    this.gemFactory = gemFactory;
    this.tableRegistry = tableRegistry;
    this.identifier = identifier;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ISocketGem manaBore = gemFactory.create(Material.EMERALD, Effects.MANA_BORE, 3, false);
      ISocketGem autoSmelt = gemFactory.create(Material.EMERALD, Effects.AUTO_SMELT, 3, false);
      ISocketGem burrow = gemFactory.create(Material.EMERALD, Effects.BURROWING, 1, false);

      IGemItem item = itemFactory.create(Material.DIAMOND_PICKAXE);
      item.editContainer(container -> {
        container.initializeCounter(SocketColors.BLUE, 3);
        container.initializeCounter(SocketColors.YELLOW, 1);
        container.initializeCounter(SocketColors.GREEN, 1);
      });

      player.getInventory().addItem(manaBore.getStack());
      player.getInventory().addItem(autoSmelt.getStack());
      player.getInventory().addItem(burrow.getStack());

      player.getInventory().addItem(item.getStack());
    }
    return false;
  }
}
