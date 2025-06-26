package org.aincraft.container.trigger;

import java.util.List;
import java.util.stream.Collectors;
import org.aincraft.api.container.trigger.IOnBlockDrop.IBlockDropContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class BlockDropContext extends AbstractContext<BlockDropItemEvent> implements
    IBlockDropContext {

  public BlockDropContext(BlockDropItemEvent event) {
    super(event);
  }

  @Override
  public Player getPlayer() {
    return event.getPlayer();
  }

  @Override
  public Block getBlock() {
    return event.getBlock();
  }

  @Override
  public BlockState getBlockState() {
    return event.getBlockState();
  }

  @Override
  public void setDrops(List<ItemStack> drops) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    World world = location.getWorld();
    Location center = location.clone().add(0.5, 0.5, 0.5);
    List<Item> itemList = drops.stream().map(stack -> {
      Item item = world.createEntity(center, Item.class);
      item.setItemStack(stack);
      return item;
    }).toList();
    event.getItems().clear();
    event.getItems().addAll(itemList);
  }

  @Override
  public @NotNull List<ItemStack> getDrops() {
    return event.getItems().stream().map(Item::getItemStack).collect(Collectors.toList());
  }
}
