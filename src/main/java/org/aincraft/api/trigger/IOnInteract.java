package org.aincraft.api.trigger;

import org.aincraft.api.container.EffectInstanceMeta;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IOnInteract {

  interface PlayerInteractContext {

    @NotNull
    Player getPlayer();

    @Nullable
    ItemStack getItem();

    @NotNull
    Action getAction();

    @Nullable
    Block getClickedBlock();

    @NotNull
    BlockFace getBlockFace();

    @Nullable
    Location getInteractionPoint();

    @Nullable
    EquipmentSlot getHand();

    @NotNull
    Event.Result useItemInHand();

    void setUseInteractedBlock(@NotNull Event.Result use);

    void setUseItemInHand(@NotNull Event.Result use);

    boolean hasItem();

    boolean hasBlock();

    boolean isBlockInHand();
  }

  void onInteract(PlayerInteractContext context, EffectInstanceMeta meta);
}
