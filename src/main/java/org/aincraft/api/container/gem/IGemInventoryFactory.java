package org.aincraft.api.container.gem;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface IGemInventoryFactory {

  IGemInventory create(@NotNull LivingEntity entity);
}
