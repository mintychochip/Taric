package org.aincraft.api.container.trigger;

import java.time.Duration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IOnActivate {
  Duration getActivationCooldown();
  boolean onActivate(int rank, Player player, ItemStack tool);
}
