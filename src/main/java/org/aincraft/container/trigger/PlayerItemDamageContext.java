package org.aincraft.container.trigger;

import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;
import org.aincraft.container.trigger.ItemDamageEvent.IPlayerItemDamageEvent;
import org.bukkit.entity.Player;

public final class PlayerItemDamageContext extends
    AbstractItemDamageContext<IPlayerItemDamageEvent> implements
    IPlayerItemDamageContext {

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

}
