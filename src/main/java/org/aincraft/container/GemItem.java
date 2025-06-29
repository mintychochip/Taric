package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IEffectInstance;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.gem.IContainerHolder;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IGemItem;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainer;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainerView;
import org.aincraft.api.trigger.IOnSocket;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GemItem extends
    AbstractContainerHolder<IGemItemContainer, IGemItemContainerView> implements
    IGemItem {

  static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey("taric", "item");

  GemItem(ItemStack stack, IGemItemContainer container) {
    super(stack, container);
  }

  private static final class View extends
      AbstractView<Container, IGemItemContainerView> implements
      IGemItemContainerView {

    View(Container container) {
      super(container);
    }

    @Override
    public void update(ItemStack stack) {
      Preconditions.checkArgument(stack != null && !stack.getType().isAir());
      super.update(stack);
      container.getStackConsumers().forEach(c -> c.accept(stack));
      container.getStackConsumers().clear();
    }

    @Override
    protected Component toItemTitle() {
      return container.getItemName();
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      builder.addLine(Component.empty());
      builder.addLine(Component.text("───────────────"));
      for (Entry<IGemEffect, EffectInstanceMeta> effectEntry : container.effects.entrySet()) {
        IGemEffect effect = effectEntry.getKey();
        EffectInstanceMeta meta = effectEntry.getValue();
        if (effect == null || meta == null) {
          continue;
        }
        ISocketColor color = effect.getSocketColor();
        Component effectLabel = Component.empty()
            .append(Component.text(effect.getName())
                .append(Component.space())
                .append(Component.text(Roman.fromInteger(meta.getRank()))));
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
      builder.addLine(Component.text("───────────────"));
      builder.addLine(Component.empty());
      return builder.build();
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
    public Iterator<Entry<IGemEffect, EffectInstanceMeta>> iterator() {
      return new HashMap<>(container.effects).entrySet().iterator();
    }
  }

  static final class Container extends
      AbstractContainer<IGemItemContainerView> implements
      IGemItemContainer {

    @Expose
    @SerializedName("effects")
    private final Map<IGemEffect, EffectInstanceMeta> effects = new HashMap<>();

    @Expose
    @SerializedName("counters")
    private final Map<ISocketColor, SocketLimitCounter> counters = new HashMap<>();

    @Expose
    @SerializedName("material")
    private final Material material;

    @Expose
    @SerializedName("item-name")
    private Component itemName;

    private List<Consumer<ItemStack>> stackConsumers = null;

    Container(NamespacedKey containerKey, Material material, Component itemName) {
      super(containerKey);
      this.material = material;
      this.itemName = itemName;
    }

    @Override
    public void initializeCounter(ISocketColor color, int max) throws IllegalStateException {
      Preconditions.checkState(!counters.containsKey(color));
      counters.put(color, new SocketLimitCounter(max));
    }

    @Override
    public boolean isCounterInitialized(ISocketColor color) {
      return counters.containsKey(color);
    }

    @Override
    public void editCounter(ISocketColor color,
        Consumer<ISocketLimitCounter> counterConsumer) {
      if (counters.containsKey(color)) {
        counterConsumer.accept(counters.get(color));
      }
    }

    @Override
    public void move(@NotNull IGemEffect effect,
        IContainerHolder<? extends IEffectContainer<?>, ?> holder)
        throws IllegalArgumentException, IllegalStateException, NullPointerException {
      Preconditions.checkNotNull(effect);
      Preconditions.checkState(hasEffect(effect), "");
      holder.editContainer(container -> {
        container.applyEffect(effect, effects.get(effect));
        this.removeEffect(effect);
      });
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

    List<Consumer<ItemStack>> getStackConsumers() {
      if (stackConsumers == null) {
        stackConsumers = new ArrayList<>();
      }
      return stackConsumers;
    }

    @Override
    protected IGemItemContainerView buildView() {
      return new View(this);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Container{")
          .append("material=").append(material)
          .append(", effects={");

      for (Map.Entry<IGemEffect, EffectInstanceMeta> entry : effects.entrySet()) {
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

    @Override
    public boolean canApplyEffect(IGemEffect effect, EffectInstanceMeta meta) {
      if (effect == null || meta == null) {
        return false;
      }
      ISocketColor socketColor = effect.getSocketColor();
      ISocketLimitCounterView counter = this.getCounter(socketColor);
      return !(counter == null || counter.getRemaining() <= 0 || hasEffect(effect)
          || !effect.isValidTarget(material));
    }

    @Override
    public void applyEffect(@NotNull IGemEffect effect, EffectInstanceMeta meta, boolean force)
        throws IllegalArgumentException {
      ISocketColor socketColor = effect.getSocketColor();
      if (!force) {
        Preconditions.checkNotNull(effect, "effect cannot be null");
        Preconditions.checkNotNull(meta, "meta cannot be null");
        Preconditions.checkArgument(meta.getRank() > 0,
            "rank: %d must be greater than 0".formatted(meta.getRank()));
        Preconditions.checkArgument(meta.getRank() <= effect.getMaxRank(),
            "rank: %d cannot be greater than max rank: %d".formatted(meta.getRank(),
                effect.getMaxRank()));
        ISocketLimitCounterView counter = this.getCounter(socketColor);
        Preconditions.checkNotNull(counter,
            "counter is not initialized for socket color: %s".formatted(socketColor.getName()));
        Preconditions.checkArgument(counter.getRemaining() > 0,
            "there are no slots remaining of this color to slot the effect");
        Preconditions.checkArgument(!hasEffect(effect),
            "this item already has the effect: %s".formatted(effect.getName()));
        Preconditions.checkArgument(effect.isValidTarget(material),
            "the item is not a valid material for this effect");
      }
      if (effect instanceof IOnSocket trigger) {
        getStackConsumers().add(trigger::onSocket);
      }
      this.editCounter(socketColor, ISocketLimitCounter::incrementCurrent);
      effects.put(effect, meta);
    }

    @Override
    public void removeEffect(@NotNull IGemEffect effect)
        throws IllegalArgumentException, NullPointerException {
      Preconditions.checkNotNull(effect);
      Preconditions.checkArgument(effects.containsKey(effect));
      ISocketColor socketColor = effect.getSocketColor();
      if (effect instanceof IOnSocket trigger) {
        getStackConsumers().add(trigger::onUnSocket);
      }
      this.editCounter(socketColor, ISocketLimitCounter::decrementCurrent);
      effects.remove(effect);
    }

    @Override
    public boolean hasEffect(@Nullable IGemEffect effect) {
      return effects.containsKey(effect);
    }

    @Override
    public int getRank(IGemEffect effect) {
      if (!effects.containsKey(effect)) {
        return 0;
      }
      EffectInstanceMeta meta = effects.get(effect);
      return meta.getRank();
    }

    @Override
    public void clear() {
      effects.clear();
    }

    public Component getItemName() {
      return itemName;
    }


  }

  static final class SocketLimitCounter implements ISocketLimitCounter {

    @Expose
    @SerializedName("max")
    private int max;
    @Expose
    @SerializedName("current")
    private int current;
    private ISocketLimitCounterView view = null;

    private SocketLimitCounter(int max) {
      this.max = max;
      this.current = 0;
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
    public void setMax(int max) {
      this.max = Math.max(max, this.current);
    }

    @Override
    public int getCurrent() {
      return getView().getCurrent();
    }

    @Override
    public void setCurrent(int sockets) {
      this.current = Math.min(sockets, this.max);
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

  static final class Factory extends
      ContainerHolderFactory<IGemItem, IGemItemContainer, IGemItemContainerView> implements
      IGemItemFactory {

    @Override
    protected NamespacedKey getContainerKey() {
      return GEM_ITEM_KEY;
    }

    @Override
    protected IGemItem create(ItemStack stack, IGemItemContainer container) {
      return new GemItem(stack, container);
    }

    @Override
    protected Class<? extends IGemItemContainer> getContainerImplClazz() {
      return Container.class;
    }

    @Override
    public IGemItem create(Material material) throws IllegalArgumentException {
      return create(new ItemStack(material));
    }

    @Override
    public IGemItem create(ItemStack stack) throws IllegalArgumentException {
      if (Factory.hasContainer(GemItem.GEM_ITEM_KEY, stack)) {
        throw new IllegalArgumentException("stack already has container");
      }
      if (!TargetType.ALL.contains(stack.getType())) {
        throw new IllegalArgumentException("illegal material");
      }
      Component itemName = stack.getData(DataComponentTypes.ITEM_NAME);
      return new GemItem(stack,
          new GemItem.Container(GemItem.GEM_ITEM_KEY, stack.getType(), itemName));
    }
  }

  @Override
  public IEffectQueueLoader getLoader(ITriggerType<?> trigger, EquipmentSlot slot) {
    return queue -> {
      for (Entry<IGemEffect, EffectInstanceMeta> entry : container.getView()) {
        EffectInstanceMeta meta = entry.getValue();
        IGemEffect effect = entry.getKey();
        if (trigger.getTriggerClazz().isInstance(effect) && effect.isValidTarget(trigger,
            stack.getType()) && effect.isValidSlot(slot)) {
          queue.add(new IEffectInstance() {
            @Override
            public IGemEffect getEffect() {
              return effect;
            }

            @Override
            public EffectInstanceMeta getMeta() {
              return meta;
            }
          });
        }
      }
    };
  }
}
