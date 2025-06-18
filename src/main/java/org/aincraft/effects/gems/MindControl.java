//package org.aincraft.effects.gems;
//
//import org.aincraft.effects.triggers.IOnEntityHitEntity;
//import java.util.Map;
//import java.util.Set;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.PathfinderMob;
//import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
//import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.raid.Raider;
//import org.bukkit.craftbukkit.entity.CraftLivingEntity;
//import org.bukkit.entity.Entity;
//import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
//
//public class MindControl extends AbstractGemEffect implements IOnEntityHitEntity {
//
//  public MindControl(String key) {
//    super(key);
//  }
//
//  @Override
//  public Set<String> getConflicts() {
//    return Set.of();
//  }
//
//  @Override
//  public void onHitEntity(int rank, Entity damager, Entity damagee,
//      Map<DamageModifier, Double> modifiers) {
//    if (!(damagee instanceof CraftLivingEntity craftLivingEntity)) {
//      return;
//    }
//
//    net.minecraft.world.entity.LivingEntity handle = craftLivingEntity.getHandle();
//
//    if (!(handle instanceof Mob mob)) {
//      return;
//    }
//    mob.targetSelector.removeAllGoals(g -> true);
//    mob.goalSelector.removeAllGoals(g -> true);
//    mob.goalSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(
//        (PathfinderMob) mob, 1.0D));
//
//    // ✅ 2. Look at player (visual immersion)
//    mob.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(mob,
//        net.minecraft.world.entity.player.Player.class, 8.0F));
//
//    // ✅ 3. Randomly look around
//    mob.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(mob));
//    mob.goalSelector.addGoal(0, new MeleeAttackGoal((PathfinderMob) mob, 1.2D, true));
//    mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(
//        (PathfinderMob) mob,
//        Monster.class,
//        true
//    ));
//    mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
//        (PathfinderMob) mob,
//        Raider.class,
//        true
//    ));
//
//  }
//}
