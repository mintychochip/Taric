package org.aincraft.api.container.gem;

import java.awt.Component;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainer;
import org.aincraft.api.container.gem.IGemItem.IGemItemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IGemItem extends IItemContainerHolder<IGemItemContainer, IGemItemContainerView> {

  interface IGemItemContainer extends IEffectContainer<IGemItemContainerView> {

    boolean hasEffect(IGemEffect effect);

    void initializeCounter(ISocketColor color, int max);

    boolean counterIsInitialized(ISocketColor color);

    void editCounter(ISocketColor color, Consumer<ISocketLimitCounter> counterConsumer);

    void move(IGemEffect effect, IItemContainerHolder<? extends IEffectContainer<?>, ?> holder);

    @Nullable
    ISocketLimitCounterView getCounter(ISocketColor color);
  }

  interface IGemItemContainerView extends IEffectContainerView,
      Iterable<Entry<IGemEffect, Integer>> {

    boolean hasEffect(IGemEffect effect);

    ISocketLimitCounterView getCounter(ISocketColor color);
  }

  interface ISocketLimitCounterView {

    int getMax();

    int getCurrent();

    int getRemaining();
  }

  interface ISocketLimitCounter {

    void setMax(int sockets);

    void setCurrent(int sockets);

    void incrementCurrent();

    void decrementCurrent();

    int getMax();

    int getCurrent();

    int getRemaining();

    ISocketLimitCounterView getView();
  }

  interface IGemItemFactory extends
      IItemHolderFactory<IGemItem, IGemItemContainer, IGemItemContainerView> {

    IGemItem create(Material material) throws IllegalArgumentException;

    IGemItem create(ItemStack stack) throws IllegalArgumentException;
  }
}
