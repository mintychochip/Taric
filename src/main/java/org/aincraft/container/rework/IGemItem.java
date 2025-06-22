package org.aincraft.container.rework;

import java.util.function.Consumer;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGemItem.IGemItemContainer;
import org.aincraft.container.rework.IGemItem.IGemItemContainerView;
import org.aincraft.effects.IGemEffect;

public interface IGemItem extends IEffectContainerHolder<IGemItemContainer, IGemItemContainerView> {

  interface IGemItemContainer extends IEffectContainer<IGemItemContainerView> {

    boolean hasEffect(IGemEffect effect);

    boolean initializeCounter(ISocketColor color, int max);

    void editCounter(ISocketColor color, Consumer<ISocketLimitCounter> counterConsumer);

    ISocketLimitCounterView getCounter(ISocketColor color);

    void move(IGemEffect effect,
        IEffectContainerHolder<? extends IEffectContainer<?>, IEffectContainerView> target);
  }

  interface IGemItemContainerView extends IEffectContainerView {

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
