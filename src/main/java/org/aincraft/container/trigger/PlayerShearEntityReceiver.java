package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IShearEntityReceiver.IPlayerShearReceiver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerShearEntityReceiver extends
    ShearEntityReceiver<PlayerShearEntityEvent, org.bukkit.event.player.PlayerShearEntityEvent> implements
    IPlayerShearReceiver {

  @Override
  public @NotNull Player getPlayer() {
    return event.getPlayer();
  }

  public static PlayerShearEntityEvent createEvent(
      org.bukkit.event.player.PlayerShearEntityEvent event) {
    return new PlayerShearEntityEventDecorator(event);
  }
}
