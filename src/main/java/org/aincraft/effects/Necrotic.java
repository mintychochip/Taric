package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnEntityHitByEntity;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Material;

public class Necrotic extends AbstractGemEffect implements IOnEntityHitByEntity {

  @Override
  public void onHitByEntity(IEntityDamageEntityContext context, int rank) {

  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.of();
  }
}
