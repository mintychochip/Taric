package org.aincraft.container.rework;

import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGemItem.IGemItemContainer;
import org.aincraft.container.rework.IGemItem.IGemItemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class GemItem extends AbstractHolder<IGemItemContainer, IGemItemContainerView> implements
    IGemItem {

  private static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey("taric", "item");

  public GemItem(ItemStack stack, IGemItemContainer container) {
    super(stack, container);
  }

  public static IGemItem create(Material material) {
    return new GemItem(new ItemStack(material), new Container(GEM_ITEM_KEY, material));
  }

  private static final class View extends
      AbstractView<Container, IGemItemContainer, IGemItemContainerView> implements
      IGemItemContainerView {

    @Override
    protected Component toItemTitle() {
      return null;
    }

    @Override
    protected ItemLore toItemLore() {
      return null;
    }


    View(Container container) {
      super(container);
    }

    @Override
    public int getRank(IGemEffect effect) {
      return container.getRank(effect);
    }

    @Override
    public boolean hasEffect(IGemEffect effect) {
      return container.hasEffect(effect);
    }

    @Override
    public ISocketLimitCounterView getCounter(ISocketColor color) {
      return container.getCounter(color);
    }
  }

  private static final class Container extends AbstractContainer<IGemItemContainerView> implements
      IGemItemContainer {

    private final Map<IGemEffect, Integer> effects = new HashMap<>();

    private final Map<ISocketColor, SocketLimitCounter> counters = new HashMap<>();

    private final Material material;

    Container(NamespacedKey containerKey, Material material) {
      super(containerKey);
      this.material = material;
    }

    @Override
    public boolean hasEffect(IGemEffect effect) {
      return effects.containsKey(effect);
    }

    @Override
    public boolean initializeCounter(ISocketColor color, int max) {
      if (counters.containsKey(color)) {
        return false;
      }
      counters.put(color, new SocketLimitCounter(max));
      return true;
    }

    @Override
    public void editCounter(ISocketColor color,
        Consumer<ISocketLimitCounter> counterConsumer) {
      if (counters.containsKey(color)) {
        counterConsumer.accept(counters.get(color));
      }
    }

    @Override
    public ISocketLimitCounterView getCounter(ISocketColor color) {
      return counters.get(color).getView();
    }

    @Override
    public boolean setEffect(IGemEffect effect, int rank, boolean force) {
      if (!force) {
        ISocketColor socketColor = effect.getSocketColor();
        int sockets = this.getRemainingSockets(socketColor);
        if (sockets <= 0 && !hasEffect(effect)) {
          return false;
        }
        if (!effect.isValidTarget(material)) {
          return false;
        }
        rank = Math.min(effect.getMaxRank(), rank);
      }
      effects.put(effect, rank);
      return true;
    }

    @Override
    protected IGemItemContainerView buildView() {
      return new View(this);
    }

    @Override
    public int getRank(IGemEffect effect) {
      return effects.get(effect);
    }

    @Override
    public void clear() {
      effects.clear();
    }


    @Override
    public void move(IGemEffect effect,
        IEffectContainerHolder<? extends IEffectContainer<?>, IEffectContainerView> target) {

    }
  }

  private static final class SocketLimitCounter implements ISocketLimitCounter {

    private int max;
    private int current;

    private SocketLimitCounter(int max) {
      this.max = max;
      this.current = 0;
    }

    private ISocketLimitCounterView view = null;

    @Override
    public void setMax(int max) {
      this.max = Math.max(max, this.current);
    }

    @Override
    public void setCurrent(int sockets) {
      this.current = Math.min(sockets, this.max);
    }

    @Override
    public void incrementCurrent() {
      this.current = Math.min(max, current + 1);
    }

    @Override
    public void decrementCurrent() {
      this.current = Math.min(0, current - 1);
    }

    @Override
    public int getMax() {
      return getView().getMax();
    }

    @Override
    public int getCurrent() {
      return getView().getCurrent();
    }

    @Override
    public int getRemaining() {
      return getView().getRemaining();
    }

    @Override
    public ISocketLimitCounterView getView() {
      if (view == null) {
        view = new SocketLimitCounterView(this);
      }
      return view;
    }
  }

  private record SocketLimitCounterView(SocketLimitCounter counter) implements
      ISocketLimitCounterView {

    @Override
    public int getMax() {
      return counter.max;
    }

    @Override
    public int getCurrent() {
      return counter.current;
    }

    @Override
    public int getRemaining() {
      return getMax() - getCurrent();
    }
  }
}
