package org.aincraft.api.context;

import org.bukkit.entity.Player;

public interface PlayerMoveContext extends IMoveContext {

  Player getPlayer();
}
