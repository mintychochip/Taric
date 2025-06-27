package org.aincraft.effects;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.aincraft.Taric;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.launchable.ILaunchable;
import org.aincraft.api.container.trigger.IOnShootBow;
import org.aincraft.container.registerable.ITriggerType;
import org.aincraft.container.registerable.TriggerTypes;
import org.aincraft.container.launchable.LaunchableFactory;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

final class Flare extends AbstractGemEffect implements IOnShootBow {

  private static CircularFireworkPool FIREWORK_POOL;

  Flare() {
    FIREWORK_POOL = new CircularFireworkPool(32);
  }

  private static final class CircularFireworkPool {

    private final FireworkMeta[] pool;
    private int pointer;

    private CircularFireworkPool(int rockets) {
      pool = new FireworkMeta[rockets];
      pointer = 0;
      for (int i = 0; i < rockets; ++i) {
        ItemStack stack = new ItemStack(Material.FIREWORK_ROCKET);
        pool[i] = (FireworkMeta) stack.getItemMeta();
      }
    }

    public FireworkMeta getNext() {
      FireworkMeta meta = pool[pointer];
      pointer = (pointer + 1) % pool.length;
      return meta;
    }
  }

  @Override
  public void onShootBow(int rank, LivingEntity launcher, List<ILaunchable> instances) {
    for (int i = 0; i < instances.size(); ++i) {
      ILaunchable launchable = instances.get(i);
      instances.set(i,
          LaunchableFactory.createFirework(launchable.getVelocity(), launchable.getLocation(), true,
              60,
              randomFirework(FIREWORK_POOL.getNext(), rank)));
    }
  }

  public static FireworkMeta randomFirework(FireworkMeta meta, int effects) {
    Random random = new Random();
    meta.setPower(random.nextInt(3) + 1);
    meta.clearEffects();
    for (int i = 0; i < effects; ++i) {
      meta.addEffect(createRandomEffect());
    }
    return meta;
  }

  public static FireworkEffect createRandomEffect() {
    return FireworkEffect.builder()
        .withColor(randomColor())
        .withFade(randomColor())
        .with(randomType())
        .flicker(Taric.getRandom().nextBoolean())
        .trail(Taric.getRandom().nextBoolean())
        .build();
  }

  public static Color randomColor() {
    Random rand = Taric.getRandom();
    int r = rand.nextInt(256);
    int g = rand.nextInt(256);
    int b = rand.nextInt(256);
    return Color.fromRGB(r, g, b);
  }

  private static FireworkEffect.Type randomType() {
    Type[] types = Type.values();
    return types[Taric.getRandom().nextInt(types.length)];
  }

  @Override
  protected Map<ITriggerType<?>, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerTypes.SHOOT_BOW, TargetType.RANGED_WEAPON)
    );
  }
}
