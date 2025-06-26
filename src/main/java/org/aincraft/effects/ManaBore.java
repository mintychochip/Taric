package org.aincraft.effects;

import java.util.Map;
import java.util.Set;
import org.aincraft.api.container.TargetType;
import org.aincraft.api.container.trigger.IItemDamageContext.IPlayerItemDamageContext;
import org.aincraft.api.container.trigger.IOnEntityItemDamage;
import org.aincraft.api.container.trigger.IOnPlayerItemDamage;
import org.aincraft.api.container.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

final class ManaBore extends AbstractGemEffect implements IOnEntityItemDamage, IOnPlayerItemDamage {

  @Override
  public void onEntityItemDamage(IEntityDamageItemContext context) {
    double damage = context.getDamage();
    Entity entity = context.getEntity();
    Player player = getPlayerOrOwner(entity);
    Bukkit.broadcastMessage("here");
    if (player == null) {
      return;
    }
    int currentXp = player.calculateTotalExperiencePoints();

    if (currentXp >= damage) {
      context.setDamage(0);
      int newXp = currentXp - (int) damage;
      int level = 0;
      while (newXp >= getXpToNextLevel(level)) {
        newXp -= getXpToNextLevel(level);
        level++;
      }
      float progress = newXp / (float) getXpToNextLevel(level);
      player.setLevel(level);
      player.setExp(progress);
    }
  }

  private static Player getPlayerOrOwner(Entity entity) {
    if (entity instanceof Player player) {
      return player;
    }

    if (entity instanceof Tameable tameable) {
      AnimalTamer owner = tameable.getOwner();
      if (owner instanceof Player) {
        return (Player) owner;
      }
    }

    return null;
  }

  public int getXpToNextLevel(int level) {
    if (level <= 15) {
      return 2 * level + 7;
    }
    if (level <= 30) {
      return 5 * level - 38;
    }
    return 9 * level - 158;
  }

  @Override
  protected Map<TriggerType, Set<Material>> buildValidTargets() {
    return Map.ofEntries(
        Map.entry(TriggerType.PLAYER_DAMAGE_ITEM, TargetType.ALL)
    );
  }

  @Override
  public void onPlayerItemDamage(IPlayerItemDamageContext context, int rank) {
    Player player = context.getPlayer();
    int damage = context.getDamage();
    int currentXp = player.calculateTotalExperiencePoints();

    if (currentXp >= damage) {
      context.setDamage(0);
      int newXp = currentXp - (int) damage;
      int level = 0;
      while (newXp >= getXpToNextLevel(level)) {
        newXp -= getXpToNextLevel(level);
        level++;
      }
      float progress = newXp / (float) getXpToNextLevel(level);
      player.setLevel(level);
      player.setExp(progress);
    }
  }
}
