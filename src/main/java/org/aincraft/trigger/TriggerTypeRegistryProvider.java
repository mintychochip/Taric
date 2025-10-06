package org.aincraft.trigger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.kyori.adventure.key.Key;
import org.aincraft.api.trigger.IOnBlockBreak;
import org.aincraft.api.trigger.IOnBlockDrop;
import org.aincraft.api.trigger.IOnEntityHitByEntity;
import org.aincraft.api.trigger.IOnEntityHitEntity;
import org.aincraft.api.trigger.IOnEntityItemDamage;
import org.aincraft.api.trigger.IOnEntityKill;
import org.aincraft.api.trigger.IOnEntityMove;
import org.aincraft.api.trigger.IOnInteract;
import org.aincraft.api.trigger.IOnPlayerFish;
import org.aincraft.api.trigger.IOnPlayerItemDamage;
import org.aincraft.api.trigger.IOnPlayerMove;
import org.aincraft.api.trigger.IOnPlayerShearEntity;
import org.aincraft.api.trigger.IOnShootBow;
import org.aincraft.api.trigger.TriggerType;
import org.aincraft.registry.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

final class TriggerTypeRegistryProvider implements Provider<Registry<TriggerType<?>>> {

  private final Plugin plugin;

  @Inject
  public TriggerTypeRegistryProvider(Plugin plugin) {
    this.plugin = plugin;
  }

  private record TriggerTypeImpl<T>(Key key, Class<T> triggerClazz) implements TriggerType<T> {

  }

  @Override
  public Registry<TriggerType<?>> get() {
    Registry<TriggerType<?>> registry = Registry.simple();
    registry.register(triggerType("block_break", IOnBlockBreak.class));
    registry.register(triggerType("block_drop", IOnBlockDrop.class));
    registry.register(triggerType("interact", IOnInteract.class));
    registry.register(triggerType("player_shear_entity", IOnPlayerShearEntity.class));
    registry.register(triggerType("entity_hit_entity", IOnEntityHitEntity.class));
    registry.register(triggerType("entity_hit_by_entity", IOnEntityHitByEntity.class));
    registry.register(triggerType("player_fish", IOnPlayerFish.class));
    registry.register(triggerType("shoot_bow", IOnShootBow.class));
    registry.register(triggerType("entity_kill", IOnEntityKill.class));
    registry.register(triggerType("player_item_damage", IOnPlayerItemDamage.class));
    registry.register(triggerType("entity_item_damage", IOnEntityItemDamage.class));
    registry.register(triggerType("entity_move", IOnEntityMove.class));
    registry.register(triggerType("player_move", IOnPlayerMove.class));
    return registry;
  }

  private <T> TriggerType<T> triggerType(String key, Class<T> triggerClazz) {
    return new TriggerTypeImpl<>(new NamespacedKey(plugin, key), triggerClazz);
  }
}
