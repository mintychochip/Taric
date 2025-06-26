package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.List;
import java.util.random.RandomGenerator;
import org.aincraft.Taric;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.IGemIdentifier;
import org.aincraft.api.container.gem.IPreciousGem;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemFactory;
import org.aincraft.container.util.ExponentialRandomSelector;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

  @Override
  public ISocketGem identify(IPreciousGem unidentified, Player identifier) {
    IPreciousGemContainerView view = unidentified.getContainer();
    IRarity rarity = view.getRarity();
    Preconditions.checkArgument(canIdentify(rarity, identifier));
    IIdentificationTable identificationTable = identificationTableRegistry.get(rarity.key());
    Preconditions.checkNotNull(identificationTable,
        "identification table for rarity: %s is not registered".formatted(rarity.getName()));
    IRarity selectedRarity = identificationTable.getRandom(Taric.getRandom());
    IGemEffect effect = selectEffect(selectedRarity, Taric.getRandom());
    ISocketGem gem = gemFactory.create(Material.EMERALD, effect.getSocketColor());
    ExponentialRandomSelector<Integer> selector = new ExponentialRandomSelector<>(DECAY_RATE);
    addRange(selector, 1, effect.getMaxRank());
    Integer random = selector.getRandom(Taric.getRandom());
    gem.editContainer(container -> {
      container.applyEffect(effect, random);
    });
    return gem;
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

  private static void addRange(ExponentialRandomSelector<Integer> selector, int min, int max) {
    Preconditions.checkArgument(min <= max, "min must be <= max (was %s > %s)", min, max);
    for (int i = min; i <= max; ++i) {
      selector.add(i);
    }
  }

  @Override
  public boolean canIdentify(IRarity rarity, Player identifier) {
    return true;
  }
}
