package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.context.EntityDamageEntityContext;
import org.aincraft.api.trigger.IOnEntityHitByEntity;
import org.aincraft.api.trigger.TriggerType;
import org.bukkit.Material;

public class Necrotic extends AbstractGemEffect implements IOnEntityHitByEntity {

  @Override
  protected Map<TriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of();
  }

  @Override
  public void onHitByEntity(EntityDamageEntityContext context, EffectInstanceMeta meta) {

  }
}
