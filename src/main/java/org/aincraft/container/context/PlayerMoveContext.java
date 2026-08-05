package org.aincraft.container.context;

import org.aincraft.api.context.IMoveContext.ChangeType;
import org.aincraft.api.context.IPlayerMoveContext;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Move context backed directly by the Bukkit event (no pass-through decorator layer).
 */
final class PlayerMoveContext extends AbstractContext<PlayerMoveEvent> implements
    IPlayerMoveContext {

  PlayerMoveContext(PlayerMoveEvent event) {
    super(event);
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public boolean hasChanged(ChangeType type) {
    return switch (type) {
      case BLOCK -> event.hasChangedBlock();
      case POSITION -> event.hasChangedPosition();
      case ORIENTATION -> event.hasChangedOrientation();
      case EXPLICITLY_BLOCK -> event.hasExplicitlyChangedBlock();
      case EXPLICITLY_POSITION -> event.hasExplicitlyChangedPosition();
    };
  }

  @Override
  public Location getFrom() {
    return event.getFrom();
  }

  @Override
  public void setFrom(Location from) {
    event.setFrom(from);
  }

  @Override
  public Location getTo() {
    return event.getTo();
  }

  @Override
  public void setTo(Location to) {
    event.setTo(to);
  }
}
