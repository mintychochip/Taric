package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemCommand implements CommandExecutor {

  private final IPreciousGemFactory factory;

  private final IRegistry<IGemEffect> effectRegistry;
  private final IGemItemFactory itemFactory;
  private final ISocketGemFactory gemFactory;
  private final IRegistry<IIdentificationTable> tableRegistry;
  private final IGemIdentifier identifier;

  @Inject
  public GemCommand(
      IPreciousGemFactory factory, IRegistry<IGemEffect> effectRegistry,
      IGemItemFactory itemFactory, ISocketGemFactory gemFactory,
      IRegistry<IIdentificationTable> tableRegistry,
      IGemIdentifier identifier) {
    this.factory = factory;
    this.effectRegistry = effectRegistry;
    this.itemFactory = itemFactory;
    this.gemFactory = gemFactory;
    this.tableRegistry = tableRegistry;
    this.identifier = identifier;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      IGemItem item = itemFactory.create(Material.DIAMOND);
    }
    return false;
  }
}
