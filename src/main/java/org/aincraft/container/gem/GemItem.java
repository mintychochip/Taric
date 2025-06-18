package org.aincraft.container.gem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainerHolder;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IContainer;
import org.aincraft.api.container.gem.IGemItem.IView;
import org.aincraft.api.exceptions.CapacityException;
import org.aincraft.api.exceptions.TargetTypeException;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GemItem extends AbstractEffectContainerHolder<IView, IContainer> implements
    IGemItem {

  private static final Supplier<Map<Key, Integer>> MAP_SUPPLIER = LinkedHashMap::new;
  private static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey(Taric.getPlugin(), "item");

  GemItem(ItemStack stack, IContainer container) {
    super(stack, container);
  }

  @Nullable
  public static IGemItem fromIfExists(ItemStack stack) {
    return GemItemFactory.holderFromIfExists(stack, GEM_ITEM_KEY, Container.class,
        container -> new GemItem(stack,container));
  }

  @NotNull
  public static IGemItem from(ItemStack stack, Callable<? extends IGemItem> loader)
      throws ExecutionException {
    return GemItemFactory.holderFrom(stack, GEM_ITEM_KEY, loader,
        GemItem.Container.class,
        container -> new GemItem(stack, container));
  }

  @Nullable
  public static IGemItem create(ItemStack stack, int sockets) {
    if (GemItemFactory.hasContainer(GEM_ITEM_KEY, stack)) {
      return null;
    }
    return new GemItem(stack, new Container(MAP_SUPPLIER.get(), sockets, stack.getType()));
  }

  public static boolean is(ItemStack stack) {
    return GemItemFactory.hasContainer(GEM_ITEM_KEY, stack);
  }

  @NotNull
  public static IEffectContainerHolder.Builder<IGemItem, IContainer, IView> builder(
      Material material,
      int sockets) {
    return new Builder(new ItemStack(material),
        new Container(MAP_SUPPLIER.get(), sockets, material));
  }

  private static final class Builder extends
      AbstractEffectContainerHolder.Builder<IGemItem, IContainer, IView> {

    Builder(ItemStack stack, IContainer container) {
      super(stack, container);
    }


    @Override
    public IGemItem build() {
      return new GemItem(stack, container);
    }
  }

  private static final class View extends
      AbstractEffectContainerView<Container, IContainer, IView> implements
      IView {

    private final int sockets;

    View(Map<Key, Integer> effects, NamespacedKey key, int sockets, Container container) {
      super(effects, key, container);
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

  private static final class Container extends AbstractEffectContainer<IContainer, IView> implements
      IContainer {

    @Expose
    @SerializedName("effects")
    private final Map<Key, Integer> effects;

    @Expose
    @SerializedName("sockets")
    private final int sockets;

    @Expose
    @SerializedName("material")
    private final Material material;

    private Container(Map<Key, Integer> effects, int sockets, Material material) {
      this.effects = effects;
      this.sockets = sockets;
      this.material = material;
    }

    @Override
    protected Map<Key, Integer> delegate() {
      return effects;
    }

    @Override
    protected IView buildView() {
      return new View(effects, GEM_ITEM_KEY, sockets, this);
    }


    @Override
    public void addEffect(IGemEffect effect, int rank, boolean force) {
      if (!effect.isValidTarget(material) && !force) {
        throw new TargetTypeException(effect, material);
      }
      if (this.getSocketsUsed() >= this.getMaxSockets() && !force && !has(effect)) {
        throw new CapacityException(this.getSocketsUsed(), this.getMaxSockets());
      }
      super.addEffect(effect, rank, force);
    }

    @Override
    public IContainer clone() {
      Container container = new Container(MAP_SUPPLIER.get(), sockets, material);
      this.copy(container, true);
      return container;
    }

    @Override
    public String toString() {
      return super.toString();
    }
  }
}
