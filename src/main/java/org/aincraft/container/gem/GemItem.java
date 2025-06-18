package org.aincraft.container.gem;

import com.google.gson.annotations.SerializedName;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.aincraft.Taric;
import org.aincraft.container.gem.exceptions.CapacityException;
import org.aincraft.container.gem.exceptions.GemConflictException;
import org.aincraft.container.gem.IGemItem.IView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GemItem extends AbstractEffectContainerHolder<IGemItem, IView> implements
    IGemItem {

  private static final Supplier<Map<String, Integer>> MAP_SUPPLIER = LinkedHashMap::new;
  private static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey(Taric.getPlugin(), "item");

  GemItem(ItemStack stack, IEffectContainer<IView> container) {
    super(stack, container);
  }

  @Override
  public IEffectContainerHolder.Builder<IGemItem, IView> toBuilder() {
    return new Builder(stack.clone(), container.clone());
  }

  @Nullable
  public static GemItem fromIfExists(ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir() || !IEffectContainer.hasContainer(GEM_ITEM_KEY, stack)) {
      return null;
    }
    Container container = IEffectContainer.from(GEM_ITEM_KEY, stack, Container.class);
    return new GemItem(stack, container);
  }

  @NotNull
  public static GemItem from(ItemStack stack, Callable<? extends GemItem> loader)
      throws ExecutionException, IllegalArgumentException {
    Material material = stack.getType();
    if (material.isAir()) {
      throw new IllegalArgumentException("stack cannot be air!");
    }
    if (!IEffectContainer.hasContainer(GEM_ITEM_KEY, stack)) {
      try {
        return loader.call();
      } catch (Exception e) {
        throw new ExecutionException(e);
      }
    }
    GemItem gemItem = fromIfExists(stack);
    assert gemItem != null;
    return gemItem;
  }

  @Nullable
  public static GemItem create(ItemStack stack, int sockets) {
    if (IEffectContainer.hasContainer(GEM_ITEM_KEY, stack)) {
      return null;
    }
    return new GemItem(stack, new Container(MAP_SUPPLIER.get(), sockets));
  }

  @NotNull
  public static IEffectContainerHolder.Builder<IGemItem, IView> builder(Material material,
      int sockets) {
    return new Builder(new ItemStack(material), new Container(MAP_SUPPLIER.get(), sockets));
  }

  private static final class Builder extends
      AbstractEffectContainerHolder.Builder<IGemItem, IView> {

    Builder(ItemStack stack, IEffectContainer<IView> container) {
      super(stack, container);
    }

    @Override
    public IGemItem build() {
      return new GemItem(stack, container);
    }
  }

  private static final class View extends AbstractEffectContainerView implements IView {

    private final int sockets;

    View(Map<String, Integer> effects, NamespacedKey key, int sockets) {
      super(effects, key);
      this.sockets = sockets;
    }

    @Override
    public int getMaxSockets() {
      return sockets;
    }

    @Override
    public int getSocketsUsed() {
      return effects.size();
    }
  }

  private static final class Container extends AbstractEffectContainer<IView> implements
      IContainer {

    @SerializedName("effects")
    private final Map<String, Integer> effects;

    @SerializedName("sockets")
    private final int sockets;

    private Container(Map<String, Integer> effects, int sockets) {
      this.effects = effects;
      this.sockets = sockets;
    }

    @Override
    protected Map<String, Integer> delegate() {
      return effects;
    }

    @Override
    protected IView buildView() {
      return new View(effects, GEM_ITEM_KEY, sockets);
    }

    @Override
    public void addEffect(IGemEffect effect, int rank, boolean force) {
      if (this.getSocketsUsed() >= this.getMaxSockets() && !force && !has(effect)) {
        throw new CapacityException(this.getSocketsUsed(), this.getMaxSockets());
      }
      super.addEffect(effect, rank, force);
    }

    @Override
    public IEffectContainer<IView> clone() {
      Container container = new Container(MAP_SUPPLIER.get(), sockets);
      this.copy(container, true);
      return container;
    }
  }
}
