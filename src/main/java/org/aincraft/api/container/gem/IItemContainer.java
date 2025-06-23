package org.aincraft.api.container.gem;

import java.util.UUID;

public interface IItemContainer<V extends IItemContainerView> {

  UUID getUuid();

  V getView();
}
