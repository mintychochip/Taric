package org.aincraft.container.trigger;

import org.bukkit.entity.Player;

public interface PlayerShearEntityEvent extends
    ShearEntityEvent<org.bukkit.event.player.PlayerShearEntityEvent> {

  Player getPlayer();
}
