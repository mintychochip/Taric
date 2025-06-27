package org.aincraft.api.context;

import org.bukkit.entity.Player;

public interface IPlayerMoveContext extends IMoveContext {

  Player getPlayer();
}
