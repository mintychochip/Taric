package org.aincraft.container.context;

import org.aincraft.api.context.IItemDamageContext.IPlayerItemDamageContext;
import org.bukkit.entity.Player;

final class PlayerItemDamageContext extends
    AbstractItemDamageContext<PlayerItemDamageEventDecorator> implements
    IPlayerItemDamageContext {

  PlayerItemDamageContext(PlayerItemDamageEventDecorator event) {
    super(event);
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public double getOriginalDamage() {
    return event.getOriginalDamage();
  }

}
