package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.trigger.IOnPlayerShearEntity;
import org.aincraft.api.context.IShearEntityContext.IPlayerShearEntityContext;
import org.aincraft.api.trigger.ITriggerType;
import org.aincraft.api.trigger.TriggerTypes;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Sheep;

final class Prismatic extends AbstractGemEffect implements IOnPlayerShearEntity {

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.of(TriggerTypes.PLAYER_SHEAR_ENTITY, TypeSet.single(Material.SHEARS));
  }


  @Override
  public void onPlayerShear(IPlayerShearEntityContext context, EffectInstanceMeta meta) {
    Bukkit.broadcast(Component.text("here"));
    if (!(context.getSheared() instanceof Sheep sheep)) {
      return;
    }
    sheep.setColor(randomColor());
  }

  private static DyeColor randomColor() {
    DyeColor[] colors = DyeColor.values();
    return colors[Taric.getRandom().nextInt(colors.length)];
  }
}
