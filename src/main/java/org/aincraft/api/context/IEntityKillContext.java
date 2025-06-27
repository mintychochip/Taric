package org.aincraft.api.context;

import java.util.List;
import org.aincraft.api.container.context.IDropContext;
import org.aincraft.api.container.context.IExperienceContext;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IEntityKillContext extends IDropContext<List<ItemStack>>,
    IExperienceContext {

  DamageSource getDamageSource();

  LivingEntity getSlain();
}
