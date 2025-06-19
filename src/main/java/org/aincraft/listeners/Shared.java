package org.aincraft.listeners;

import org.aincraft.api.container.Mutable;
import org.aincraft.container.SharedList;
import org.bukkit.inventory.ItemStack;

/**
 * This class holds shared resources used in event handling.
 * These resources are intended for synchronous access on the main thread.
 * <p>
 * Bukkit's environment is single-threaded, and events are fired in a synchronous context.
 * Access or modification outside the main thread may lead to unexpected behavior.
 */

public class Shared {

  private final SharedList<ItemStack> drops = new SharedList<>();
  private final Mutable<Integer> experience = new Mutable<>(0);

  public SharedList<ItemStack> getDrops() {
    return drops;
  }

  public Mutable<Integer> getExperience() {
    return experience;
  }
}
