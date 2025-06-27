package org.aincraft.container.context;

abstract class AbstractContext<H> {

  protected final H event;

  AbstractContext(H event) {
    this.event = event;
  }

}
