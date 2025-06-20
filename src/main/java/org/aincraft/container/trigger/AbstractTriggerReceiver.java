package org.aincraft.container.trigger;

import org.aincraft.api.container.receiver.ITriggerReceiver;
import org.bukkit.event.Event;

public abstract class AbstractTriggerReceiver<E> implements ITriggerReceiver {

  protected E event;
  protected int rank;

  AbstractTriggerReceiver() {
    this.event = null;
    this.rank = 0;
  }

  @Override
  public int getRank() {
    return rank;
  }

  public void setHandle(E event) {
    this.event = event;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }
}
