package org.aincraft.api.exceptions;

import org.aincraft.effects.IGemEffect;

//TODO: make conflict API
public final class GemConflictException extends RuntimeException {

  private final IGemEffect conflicting;
  private final IGemEffect conflicted;

  public GemConflictException(IGemEffect conflicting, IGemEffect conflicted) {
    super("failed to add gem: %s conflicted with: %s".formatted(conflicting.getName(),
        conflicted.getName()));
    this.conflicting = conflicting;
    this.conflicted = conflicted;
  }

  public IGemEffect getConflicting() {
    return conflicting;
  }

  public IGemEffect getConflicted() {
    return conflicted;
  }
}
