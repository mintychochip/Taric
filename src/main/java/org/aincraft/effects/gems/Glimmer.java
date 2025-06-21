package org.aincraft.effects.gems;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnFish;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;

final class Glimmer extends AbstractGemEffect implements IOnFish {

  @Override
  public void onFish(IFishContext context) {
    context.getHook().setGlowing(true);
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.FISH, TypeSet.single(Material.FISHING_ROD))
    );
  }
}
