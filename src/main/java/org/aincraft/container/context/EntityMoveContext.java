package org.aincraft.container.context;

import org.aincraft.api.context.IEntityMoveContext;
import org.bukkit.entity.Entity;

final class EntityMoveContext extends AbstractMoveContext<EntityMoveEventDecorator> implements
    IEntityMoveContext {

  EntityMoveContext(EntityMoveEventDecorator event) {
    super(event);
  }

  @Override
  public Entity getEntity() {
    return event.getEntity();
  }
}
