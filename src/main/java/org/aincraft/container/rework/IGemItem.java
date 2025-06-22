package org.aincraft.container.rework;

import java.util.Map.Entry;
import java.util.function.Consumer;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGemItem.IGemItemContainer;
import org.aincraft.container.rework.IGemItem.IGemItemContainerView;
import org.aincraft.effects.IGemEffect;
import org.jetbrains.annotations.Nullable;

public interface IGemItem extends IEffectContainerHolder<IGemItemContainer, IGemItemContainerView> {

  interface IGemItemContainer extends IEffectContainer<IGemItemContainerView> {

    boolean hasEffect(IGemEffect effect);

    boolean initializeCounter(ISocketColor color, int max);

    void editCounter(ISocketColor color, Consumer<ISocketLimitCounter> counterConsumer);

    @Nullable
    ISocketLimitCounterView getCounter(ISocketColor color);
  }

  interface IGemItemContainerView extends IEffectContainerView, Iterable<Entry<IGemEffect,Integer>> {

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
}
