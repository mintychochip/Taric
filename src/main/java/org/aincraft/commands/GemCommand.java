package org.aincraft.commands;

import com.google.inject.Inject;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemFactory;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemFactory;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.effects.Effects;
import org.aincraft.effects.IGemEffect;
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

  private final IPreciousGemFactory factory;

  private final IRegistry<ITriggerType<?>> triggerRegistry;
  private final IRegistry<IGemEffect> effectRegistry;
  private final IGemItemFactory itemFactory;
  private final ISocketGemFactory gemFactory;
  private final IRegistry<IIdentificationTable> tableRegistry;
  private final IGemIdentifier identifier;

  @Inject
  public GemCommand(
      IPreciousGemFactory factory, IRegistry<ITriggerType<?>> triggerRegistry,
      IRegistry<IGemEffect> effectRegistry,
      IGemItemFactory itemFactory, ISocketGemFactory gemFactory,
      IRegistry<IIdentificationTable> tableRegistry,
      IGemIdentifier identifier) {
    this.factory = factory;
    this.triggerRegistry = triggerRegistry;
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
      IGemItem item = itemFactory.create(ItemStack.of(Material.SHEARS));
      item.editContainer(container -> {
        container.applyEffect(Effects.PRISMATIC, new EffectInstanceMeta(3), true);
      });
      player.getInventory().addItem(item.getStack());
      for (ITriggerType<?> type : triggerRegistry) {
        Bukkit.broadcastMessage(type.key().toString());
      }

    }
    return false;
  }
}
