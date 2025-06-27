package org.aincraft.api.trigger;

import net.kyori.adventure.key.Key;
import org.aincraft.container.registerable.AbstractRegisterable;
import org.aincraft.registry.IRegistry;

public final class TriggerTypes {

  public static final ITriggerType<IOnBlockBreak> BLOCK_BREAK;
  public static final ITriggerType<IOnBlockDrop> BLOCK_DROP;
  public static final ITriggerType<IOnInteract> INTERACT;
  public static final ITriggerType<IOnPlayerShearEntity> PLAYER_SHEAR_ENTITY;
  public static final ITriggerType<IOnEntityHitEntity> ENTITY_HIT_ENTITY;
  public static final ITriggerType<IOnEntityHitByEntity> ENTITY_HIT_BY_ENTITY;
  public static final ITriggerType<IOnPlayerFish> PLAYER_FISH;
  public static final ITriggerType<IOnShootBow> SHOOT_BOW;
  public static final ITriggerType<IOnEntityKill> ENTITY_KILL;
  public static final ITriggerType<IOnPlayerItemDamage> PLAYER_ITEM_DAMAGE;
  public static final ITriggerType<IOnEntityItemDamage> ENTITY_ITEM_DAMAGE;
  public static final ITriggerType<IOnEntityMove> ENTITY_MOVE;
  public static final ITriggerType<IOnPlayerMove> PLAYER_MOVE;

  static {
    BLOCK_BREAK = new TriggerType<>(Key.key("taric:block_break"), IOnBlockBreak.class);
    BLOCK_DROP = new TriggerType<>(Key.key("taric:block_drop"), IOnBlockDrop.class);
    INTERACT = new TriggerType<>(Key.key("taric:interact"), IOnInteract.class);
    PLAYER_SHEAR_ENTITY = new TriggerType<>(Key.key("taric:player_shear_entity"),
        IOnPlayerShearEntity.class);
    ENTITY_HIT_ENTITY = new TriggerType<>(Key.key("taric:entity_hit_entity"),
        IOnEntityHitEntity.class);
    ENTITY_HIT_BY_ENTITY = new TriggerType<>(Key.key("taric:entity_hit_by_entity"),
        IOnEntityHitByEntity.class);
    PLAYER_FISH = new TriggerType<>(Key.key("taric:player_fish"), IOnPlayerFish.class);
    SHOOT_BOW = new TriggerType<>(Key.key("taric:shoot_bow"), IOnShootBow.class);
    ENTITY_KILL = new TriggerType<>(Key.key("taric:entity_kill"), IOnEntityKill.class);
    PLAYER_ITEM_DAMAGE = new TriggerType<>(Key.key("taric:player_item_damage"),
        IOnPlayerItemDamage.class);
    ENTITY_ITEM_DAMAGE = new TriggerType<>(Key.key("taric:entity_item_damage"),
        IOnEntityItemDamage.class);
    ENTITY_MOVE = new TriggerType<>(Key.key("taric:entity_move"), IOnEntityMove.class);
    PLAYER_MOVE = new TriggerType<>(Key.key("taric:player_move"), IOnPlayerMove.class);
  }

  private TriggerTypes() {
    throw new UnsupportedOperationException("This class should not be instantiated.");
  }

  static void initialize(IRegistry<ITriggerType<?>> registry) {
    registry.register(BLOCK_BREAK);
    registry.register(BLOCK_DROP);
    registry.register(INTERACT);
    registry.register(PLAYER_SHEAR_ENTITY);
    registry.register(ENTITY_HIT_ENTITY);
    registry.register(ENTITY_HIT_BY_ENTITY);
    registry.register(PLAYER_FISH);
    registry.register(SHOOT_BOW);
    registry.register(ENTITY_KILL);
    registry.register(PLAYER_ITEM_DAMAGE);
    registry.register(ENTITY_ITEM_DAMAGE);
    registry.register(ENTITY_MOVE);
  }

  private static final class TriggerType<T> extends AbstractRegisterable implements
      ITriggerType<T> {

    private final Class<T> clazz;

    public TriggerType(Key key, Class<T> clazz) {
      super(key);
      this.clazz = clazz;
    }

    @Override
    public Class<T> getTriggerClazz() {
      return clazz;
    }
  }
}
