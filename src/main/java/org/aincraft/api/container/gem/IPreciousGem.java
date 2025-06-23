package org.aincraft.api.container.gem;

import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainer;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IPreciousGem extends
    IItemContainerHolder<IPreciousGemContainer, IPreciousGemContainerView> {

  interface IPreciousGemContainerView extends IItemContainerView {

    @NotNull
    ISocketColor getColor();

    @NotNull
    IRarity getRarity();
  }

  interface IPreciousGemContainer extends IItemContainer<IPreciousGemContainerView> {

    void setSocketColor(@NotNull ISocketColor color);

    void setRarity(@NotNull IRarity rarity);
  }

  interface IPreciousGemFactory extends
      IItemHolderFactory<IPreciousGem, IPreciousGemContainer, IPreciousGemContainerView> {

    IPreciousGem create(Material material, IRarity rarity, ISocketColor socketColor);
  }
}
