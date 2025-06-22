package org.aincraft.container.rework;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.TargetType;
import org.aincraft.container.rework.IGemItem.IGemItemContainer;
import org.aincraft.container.rework.IGemItem.IGemItemContainerView;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GemItem extends AbstractHolder<IGemItemContainer, IGemItemContainerView> implements
    IGemItem {

  private static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey("taric", "item");

  public GemItem(ItemStack stack, IGemItemContainer container) {
    super(stack, container);
  }

  @Nullable
  public static IGemItem fromIfExists(ItemStack stack) {
    return GemItemFactory.holderFromIfExists(stack, GEM_ITEM_KEY, Container.class,
        container -> new GemItem(stack, container));
  }

  @NotNull
  public static IGemItem create(Material material) throws IllegalArgumentException {
    if (!TargetType.ALL.contains(material)) {
      throw new IllegalArgumentException("illegal material");
    }
    return new GemItem(new ItemStack(material), new Container(GEM_ITEM_KEY, material));
  }

  private static final class View extends
      AbstractView<Container, IGemItemContainer, IGemItemContainerView> implements
      IGemItemContainerView {

    View(Container container) {
      super(container);
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      for (Entry<IGemEffect, Integer> effectEntry : container.effects.entrySet()) {
        IGemEffect effect = effectEntry.getKey();
        int rank = effectEntry.getValue();
        if (effect == null || rank == 0) {
          continue;
        }
        ISocketColor color = effect.getSocketColor();
        Component effectLabel = Component.empty()
            .append(Component.text(effect.getName())
                .append(Component.space())
                .append(Component.text(Roman.fromInteger(rank))));
        Component label = Component.empty()
            .append(Component.text("\u2022 ["))
            .append(Component.space())
            .append(effectLabel)
            .append(Component.text(" ]"))
            .color(color.getTextColor()).decoration(
                TextDecoration.ITALIC, false);
        builder.addLine(label);
      }
      for (Entry<ISocketColor, SocketLimitCounter> counterEntry : container.counters.entrySet()) {
        ISocketColor color = counterEntry.getKey();
        SocketLimitCounter counter = counterEntry.getValue();
        int remaining = counter.getRemaining();
        for (int i = 0; i < remaining; ++i) {
          Component label = Component.empty().append(Component.text("\u2022 [ Empty ]"))
              .color(color.getTextColor()).decoration(TextDecoration.ITALIC, false);
          builder.addLine(label);
        }
      }

      return builder.build();
    }

    @Override
    protected Component toItemTitle() {
      return super.toItemTitle();
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

    @NotNull
    @Override
    public Iterator<Entry<IGemEffect, Integer>> iterator() {
      return new HashMap<>(container.effects).entrySet().iterator();
    }
  }

  private static final class Container extends AbstractContainer<IGemItemContainerView> implements
      IGemItemContainer {

    @Expose
    @SerializedName("effects")
    private final Map<IGemEffect, Integer> effects = new HashMap<>();

    @Expose
    @SerializedName("counters")
    private final Map<ISocketColor, SocketLimitCounter> counters = new HashMap<>();

    @Expose
    @SerializedName("material")
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
    @Nullable
    public ISocketLimitCounterView getCounter(ISocketColor color) {
      if (!counters.containsKey(color)) {
        return null;
      }
      SocketLimitCounter counter = counters.get(color);
      return counter.getView();
    }

    @Override
    public boolean setEffect(IGemEffect effect, int rank, boolean force) {
      ISocketColor socketColor = effect.getSocketColor();
      if (!force) {
        ISocketLimitCounterView counter = this.getCounter(socketColor);
        if (counter != null && counter.getRemaining() <= 0 && !hasEffect(effect)) {
          return false;
        }
        if (!effect.isValidTarget(material)) {
          return false;
        }
        rank = Math.min(effect.getMaxRank(), rank);
      }
      this.editCounter(socketColor, ISocketLimitCounter::incrementCurrent);
      effects.put(effect, rank);
      return true;
    }

    @Override
    public void removeEffect(IGemEffect effect) {
      ISocketColor socketColor = effect.getSocketColor();
      this.editCounter(socketColor, ISocketLimitCounter::decrementCurrent);
      effects.remove(effect);
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
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Container{")
          .append("material=").append(material)
          .append(", effects={");

      for (Map.Entry<IGemEffect, Integer> entry : effects.entrySet()) {
        IGemEffect effect = entry.getKey();
        sb.append(effect.key().asString())
            .append(":rank=").append(entry.getValue())
            .append(", ");
      }
      if (!effects.isEmpty()) {
        sb.setLength(sb.length() - 2);
      }
      sb.append("}, counters={");

      for (Map.Entry<ISocketColor, SocketLimitCounter> entry : counters.entrySet()) {
        ISocketColor color = entry.getKey();
        SocketLimitCounter counter = entry.getValue();
        sb.append(color.toString())
            .append(":[")
            .append(counter.getCurrent()).append("/").append(counter.getMax())
            .append("], ");
      }
      if (!counters.isEmpty()) {
        sb.setLength(sb.length() - 2);
      }
      sb.append("}}");
      return sb.toString();
    }
  }

  private static final class SocketLimitCounter implements ISocketLimitCounter {

    @Expose
    @SerializedName("max")
    private int max;
    @Expose
    @SerializedName("current")
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
