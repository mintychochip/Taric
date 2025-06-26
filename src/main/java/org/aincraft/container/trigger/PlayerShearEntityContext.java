package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearContext;
import org.aincraft.container.trigger.ShearEntityEvent.IPlayerShearEntityEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class PlayerShearEntityContext extends
    AbstractShearEntityContext<IPlayerShearEntityEvent> implements
    IPlayerShearContext {

  public PlayerShearEntityContext(IPlayerShearEntityEvent event) {
    super(event);
  }

  public static IPlayerShearEntityEvent createEvent(
      org.bukkit.event.player.PlayerShearEntityEvent event) {
    return new PlayerShearEntityEventDecorator(event);
  }

  @Override
  public @NotNull Player getPlayer() {
    return event.getPlayer();
  }
}
