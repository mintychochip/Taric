package org.aincraft.effects.gems;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aincraft.Taric;
import org.aincraft.api.container.TypeSet;
import org.aincraft.api.container.trigger.IOnPlayerShear;
import org.aincraft.api.container.trigger.IShearEntityReceiver.IPlayerShearReceiver;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

final class Prismatic extends AbstractGemEffect implements IOnPlayerShear {

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.of(TriggerType.PLAYER_SHEAR, TypeSet.single(Material.SHEARS));
  }



  @Override
  public void onPlayerShear(IPlayerShearReceiver receiver) {
    if (!(receiver.getSheared() instanceof Sheep sheep)) {
      return;
    }
    sheep.setColor(randomColor());
  }

  private static DyeColor randomColor() {
    DyeColor[] colors = DyeColor.values();
    return colors[Taric.getRandom().nextInt(colors.length)];
  }
}
