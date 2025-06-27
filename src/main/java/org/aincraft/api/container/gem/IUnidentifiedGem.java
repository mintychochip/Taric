package org.aincraft.api.container.gem;

import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainer;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainerView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IUnidentifiedGem extends
    IContainerHolder<IUnidentifiedGemContainer, IUnidentifiedGemContainerView> {

  interface IUnidentifiedGemContainerView extends IGemContainerView {

    @NotNull
    IRarity getRarity();

    AppraisalState getState();

  }

  interface IUnidentifiedGemContainer extends IGemContainer<IUnidentifiedGemContainerView> {

    void setRarity(@NotNull IRarity rarity);

    void setState(AppraisalState state);
  }

  interface IUnidentifiedGemFactory extends
      IContainerHolderFactory<IUnidentifiedGem, IUnidentifiedGemContainer, IUnidentifiedGemContainerView> {

    IUnidentifiedGem create(@NotNull ItemStack stack, @NotNull ISocketColor color,
        @NotNull IRarity rarity) throws IllegalArgumentException, NullPointerException;

    IUnidentifiedGem create(@NotNull ItemStack stack, @NotNull IRarity rarity)
        throws IllegalArgumentException, NullPointerException;

    IUnidentifiedGem create(@NotNull ItemStack stack)
        throws IllegalArgumentException, NullPointerException;
  }
}
