package org.aincraft.container.gem;

import com.google.inject.Inject;
import java.util.List;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IGem;
import org.aincraft.api.container.gem.IGemEffectSelector;
import org.aincraft.container.util.WeightedRandom;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;

public class GemEffectSelector implements IGemEffectSelector {

  private final IRegistry<IGemEffect> effects;

  @Inject
  public GemEffectSelector(IRegistry<IGemEffect> effects) {
    this.effects = effects;
  }

  @Override
  public void setRandom(IGem gem) {
    List<IGemEffect> effects = this.effects.stream()
        .filter(effect -> effect.getSocketColor().equals(gem.getSocketColor())).toList();
    WeightedRandom<IGemEffect> random = new WeightedRandom<>();
    for (IGemEffect effect : effects) {
      random.put(effect, effect.getRarity().getWeight());
    }

    IGemEffect effect = random.getRandom(Taric.getRandom());
    if (effect == null) {
      return;
    }
    int rank = Taric.getRandom().nextInt(1, effect.getMaxRank() + 1);
    gem.editContainer(container -> {
      container.setEffect(effect, rank);
    });
  }
}
