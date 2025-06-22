package org.aincraft.api.events;

import org.aincraft.container.rework.IGem;
import org.aincraft.container.rework.IGemItem;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class SocketGemEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final IGem gem;
  private final IGemItem item;

  public SocketGemEvent(IGem gem, IGemItem item) {
    this.gem = gem;
    this.item = item;
  }

  public IGem getGem() {
    return gem;
  }

  public IGemItem getItem() {
    return item;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
