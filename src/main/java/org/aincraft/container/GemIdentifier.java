package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemIdentifier implements IGemIdentifier {

  private final ISocketGemFactory gemFactory;

  @Inject
  public GemIdentifier(ISocketGemFactory gemFactory) {
    this.gemFactory = gemFactory;
  }

  @Override
  public ISocketGem identify(@NotNull IUnidentifiedGem unidentified, @NotNull Player player) {
    Preconditions.checkNotNull(unidentified);
    Preconditions.checkNotNull(player);
    IUnidentifiedGemContainerView view = unidentified.getContainer();
    IGemEffect viewEffect = view.getEffect();
    int rank = view.getRank();
    return gemFactory.create(Material.EMERALD, viewEffect, rank, true);
  }

  @Override
  public boolean canIdentify(IRarity rarity, Player identifier) {
    return true;
  }
}
