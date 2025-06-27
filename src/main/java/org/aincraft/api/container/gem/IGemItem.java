package org.aincraft.api.container.gem;

import java.util.Map.Entry;
import java.util.function.Consumer;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainer;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainerView;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.container.context.IEffectQueueLoader;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGemItem extends IContainerHolder<IGemItemContainer, IGemItemContainerView> {

  interface IGemItemContainer extends IEffectContainer<IGemItemContainerView> {

    boolean hasEffect(@Nullable IGemEffect effect);

    void initializeCounter(ISocketColor color, int max) throws IllegalStateException;

    boolean isCounterInitialized(ISocketColor color);

    void editCounter(ISocketColor color, Consumer<ISocketLimitCounter> counterConsumer);

    void move(@NotNull IGemEffect effect,
        IContainerHolder<? extends IEffectContainer<?>, ?> holder)
        throws IllegalArgumentException, IllegalStateException, NullPointerException;

    @Nullable
    ISocketLimitCounterView getCounter(ISocketColor color);
  }

  interface IGemItemContainerView extends IEffectContainerView,
      Iterable<Entry<IGemEffect, EffectInstanceMeta>> {

    boolean hasEffect(IGemEffect effect);

    ISocketLimitCounterView getCounter(ISocketColor color);
  }

  interface ISocketLimitCounterView {

    int getMax();

    int getCurrent();

    int getRemaining();
  }

  interface ISocketLimitCounter {

    void incrementCurrent();

    void decrementCurrent();

    int getMax();

    void setMax(int sockets);

    int getCurrent();

    void setCurrent(int sockets);

    int getRemaining();

    ISocketLimitCounterView getView();
  }

  interface IGemItemFactory extends
      IContainerHolderFactory<IGemItem, IGemItemContainer, IGemItemContainerView> {

    IGemItem create(Material material) throws IllegalArgumentException;

    IGemItem create(ItemStack stack) throws IllegalArgumentException;
  }

  IEffectQueueLoader getLoader(ITriggerType<?> trigger, EquipmentSlot slot);
}
