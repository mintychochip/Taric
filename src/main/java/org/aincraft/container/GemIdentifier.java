package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.Taric;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainerView;
import org.aincraft.container.util.ExponentialRandomSelector;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GemIdentifier implements IGemIdentifier {

  private static final double DECAY_RATE = 1.5;
  private final IRegistry<IIdentificationTable> identificationTableRegistry;
  private final IRegistry<IGemEffect> effectRegistry;
  private final ISocketGemFactory gemFactory;

  @Inject
  public GemIdentifier(IRegistry<IIdentificationTable> identificationTableRegistry,
      IRegistry<IGemEffect> effectRegistry, ISocketGemFactory gemFactory) {
    this.identificationTableRegistry = identificationTableRegistry;
    this.effectRegistry = effectRegistry;
    this.gemFactory = gemFactory;
  }

  private static void addRange(ExponentialRandomSelector<Integer> selector, int min, int max) {
    Preconditions.checkArgument(min <= max, "min must be <= max (was %s > %s)", min, max);
    for (int i = min; i <= max; ++i) {
      selector.add(i);
    }
  }

  @Override
  public ISocketGem identify(@NotNull IUnidentifiedGem unidentified, @NotNull Player player) {
    Preconditions.checkNotNull(unidentified);
    Preconditions.checkNotNull(player);
    IUnidentifiedGemContainerView view = unidentified.getContainer();
    IGemEffect viewEffect = view.getEffect();
    int rank = view.getRank();
    if (viewEffect != null && rank > 0) {
      return gemFactory.create(Material.EMERALD, viewEffect, rank, true);
    }
    IRarity rarity = view.getRarity();
    ISocketColor color = view.getColor();
    List<IGemEffect> effects = effectRegistry.stream()
        .filter(effect -> effect.getSocketColor().equals(color)).toList();
    if (effects.isEmpty()) { //no colors matched
      int index = Taric.getRandom().nextInt(effectRegistry.size());
      IGemEffect effect = effectRegistry.get(index);
      return gemFactory.create(Material.EMERALD, effect, 1, false);
    }
    IIdentificationTable table = identificationTableRegistry.get(rarity.key());
    IRarity selected = table.getRandom(Taric.getRandom());
    List<IGemEffect> rarityColorEffects = effects.stream()
        .filter(effect -> effect.getRarity().equals(selected)).toList();
    if (!rarityColorEffects.isEmpty()) {
      int index = Taric.getRandom().nextInt(rarityColorEffects.size());
      IGemEffect effect = rarityColorEffects.get(index);
      return gemFactory.create(Material.EMERALD, effect, 1, false);
    }
    int index = Taric.getRandom().nextInt(effectRegistry.size());
    IGemEffect effect = effectRegistry.values().stream().toList().get(index);
    return gemFactory.create(Material.EMERALD, effect, 1, false);
  }

  @Override
  public boolean canIdentify(IRarity rarity, Player identifier) {
    return true;
  }

  private IGemEffect selectEffect(IRarity rarity, RandomGenerator randomGenerator) {
    List<IGemEffect> applicable = effectRegistry.stream()
        .filter(effect -> effect.getRarity().equals(
            rarity)).toList();
    int size = applicable.size();
    if (size == 1) {
      return applicable.getFirst();
    } else if (size > 1) {
      int index = randomGenerator.nextInt(size);
      return applicable.get(index);
    }
    return null;
  }
}
