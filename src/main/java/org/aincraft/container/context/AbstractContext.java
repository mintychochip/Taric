package org.aincraft.container.context;

abstract class AbstractContext<H> {

  protected H event;

  AbstractContext(H event) {
    this.event = event;
  }

  public void setHandle(H event) {
    this.event = event;
  }
}
