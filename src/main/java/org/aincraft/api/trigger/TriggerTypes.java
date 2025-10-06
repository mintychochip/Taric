package org.aincraft.api.trigger;

import com.google.common.base.Preconditions;
import org.aincraft.api.Bridge;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.aincraft.registry.RegistryAccessKeys;
import org.bukkit.NamespacedKey;

public final class TriggerTypes {

  public static final TriggerType<IOnBlockBreak> BLOCK_BREAK = triggerType("block_break");
  public static final TriggerType<IOnBlockDrop> BLOCK_DROP = triggerType("block_drop");
  public static final TriggerType<IOnInteract> INTERACT = triggerType("interact");
  public static final TriggerType<IOnPlayerShearEntity> PLAYER_SHEAR_ENTITY = triggerType(
      "player_shear_entity");
  public static final TriggerType<IOnEntityHitEntity> ENTITY_HIT_ENTITY = triggerType(
      "entity_hit_entity");
  public static final TriggerType<IOnEntityHitByEntity> ENTITY_HIT_BY_ENTITY = triggerType(
      "entity_hit_by_entity");
  public static final TriggerType<IOnPlayerFish> PLAYER_FISH = triggerType("player_fish");
  public static final TriggerType<IOnShootBow> SHOOT_BOW = triggerType("shoot_bow");
  public static final TriggerType<IOnEntityKill> ENTITY_KILL = triggerType("entity_kill");
  public static final TriggerType<IOnPlayerItemDamage> PLAYER_ITEM_DAMAGE = triggerType(
      "player_item_damage");
  public static final TriggerType<IOnEntityItemDamage> ENTITY_ITEM_DAMAGE = triggerType(
      "entity_item_damage");
  public static final TriggerType<IOnEntityMove> ENTITY_MOVE = triggerType("entity_move");
  public static final TriggerType<IOnPlayerMove> PLAYER_MOVE = triggerType("player_move");

  private TriggerTypes() {
    throw new UnsupportedOperationException("do not instantiate");
  }

  @SuppressWarnings("unchecked")
  private static <T> TriggerType<T> triggerType(String key) throws IllegalArgumentException {
    NamespacedKey triggerTypeKey = new NamespacedKey(Bridge.bridge().plugin(), key);
    Registry<TriggerType<?>> triggerTypeRegistry = RegistryAccess.registryAccess()
        .getRegistry(RegistryAccessKeys.TRIGGER_TYPE);
    Preconditions.checkArgument(triggerTypeRegistry.isRegistered(triggerTypeKey));
    return (TriggerType<T>) triggerTypeRegistry.get(triggerTypeKey);
  }
}
