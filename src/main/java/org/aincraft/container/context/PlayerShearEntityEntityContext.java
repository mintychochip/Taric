package org.aincraft.container.context;

import org.aincraft.api.container.trigger.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.container.context.ShearEntityEvent.IPlayerShearEntityEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class PlayerShearEntityEntityContext extends
    AbstractShearEntityContext<IPlayerShearEntityEvent> implements
    IPlayerShearEntityContext {

  public PlayerShearEntityEntityContext(IPlayerShearEntityEvent event) {
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
