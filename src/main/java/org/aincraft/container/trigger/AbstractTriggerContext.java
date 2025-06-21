package org.aincraft.container.trigger;

import org.aincraft.api.container.receiver.ITriggerContext;

abstract class AbstractTriggerContext<E> implements ITriggerContext {

  protected E event;
  protected int rank;

  AbstractTriggerContext() {
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
