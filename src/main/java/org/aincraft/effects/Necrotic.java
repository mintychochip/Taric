package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.trigger.IEntityDamageEntityContext;
import org.aincraft.api.container.trigger.IOnEntityHitByEntity;
import org.aincraft.container.registerable.ITriggerType;
import org.bukkit.Material;

public class Necrotic extends AbstractGemEffect implements IOnEntityHitByEntity {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public void onHitByEntity(IEntityDamageEntityContext context, EffectInstanceMeta meta) {

  }
}
