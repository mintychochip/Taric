package org.aincraft.api.exceptions;

public final class CapacityException extends RuntimeException {

  private final int currentCapacity;
  private final int requestedCapacity;

  public CapacityException(int currentCapacity, int requestedCapacity) {
    super("Capacity changed from " + currentCapacity + " to " + requestedCapacity);
    this.currentCapacity = currentCapacity;
    this.requestedCapacity = requestedCapacity;
  }

  public int getCurrentCapacity() {
    return currentCapacity;
  }

  public int getRequestedCapacity() {
    return requestedCapacity;
  }
}
