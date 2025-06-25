package org.aincraft.api.container.gem;

import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainer;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IPreciousGem extends
    IItemContainerHolder<IPreciousGemContainer, IPreciousGemContainerView> {

  interface IPreciousGemContainerView extends IItemContainerView {

    @NotNull
    IRarity getRarity();

    AppraisalState getState();

  }

  interface IPreciousGemContainer extends IItemContainer<IPreciousGemContainerView> {

    void setRarity(@NotNull IRarity rarity);

    void setState(AppraisalState state);
  }

  interface IPreciousGemFactory extends
      IItemHolderFactory<IPreciousGem, IPreciousGemContainer, IPreciousGemContainerView> {

    IPreciousGem create(ItemStack stack, IRarity rarity)
        throws IllegalArgumentException;

    IPreciousGem create(ItemStack stack) throws IllegalArgumentException;
  }
}
