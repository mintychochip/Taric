package org.aincraft.container.context;

import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;
import org.aincraft.container.context.ItemDamageEvent.IPlayerItemDamageEvent;
import org.bukkit.entity.Player;

final class PlayerItemDamageContext extends
    AbstractItemDamageContext<IPlayerItemDamageEvent> implements
    IPlayerItemDamageContext {

  public PlayerItemDamageContext(IPlayerItemDamageEvent event) {
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
