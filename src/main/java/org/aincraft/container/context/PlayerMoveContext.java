package org.aincraft.container.context;

import org.aincraft.api.context.IPlayerMoveContext;
import org.bukkit.entity.Player;

final class PlayerMoveContext extends AbstractMoveContext<PlayerMoveEventDecorator> implements
    IPlayerMoveContext {

  PlayerMoveContext(PlayerMoveEventDecorator event) {
    super(event);
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }
}
