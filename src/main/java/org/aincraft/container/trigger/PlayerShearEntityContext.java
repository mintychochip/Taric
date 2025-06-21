package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerShearEntityContext extends
    ShearEntityContext<PlayerShearEntityEvent, org.bukkit.event.player.PlayerShearEntityEvent> implements
    IPlayerShearContext {

  @Override
  public @NotNull Player getPlayer() {
    return event.getPlayer();
  }

  public static PlayerShearEntityEvent createEvent(
      org.bukkit.event.player.PlayerShearEntityEvent event) {
    return new PlayerShearEntityEventDecorator(event);
  }
}
