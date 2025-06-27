package org.aincraft.api.container.gem;

import org.aincraft.api.container.IRarity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IGemIdentifier {

  ISocketGem identify(@NotNull IUnidentifiedGem gem, @NotNull Player player)
      throws IllegalArgumentException;

  boolean canIdentify(IRarity rarity, Player identifier);

}
