package org.aincraft.database;

import java.sql.SQLException;
import java.util.List;
import org.aincraft.container.EffectCooldown;
import org.aincraft.effects.IGemEffect;
import org.bukkit.entity.Player;

public interface IDatabase {

  EffectCooldown createCooldown(Player player, IGemEffect effect);

  EffectCooldown getCooldown(Player player, IGemEffect effect);

  boolean updateCooldown(Player player, IGemEffect effect);

  boolean hasCooldown(Player player, IGemEffect effect);

  List<EffectCooldown> getPlayerCooldowns(Player player);

  void shutdown() throws SQLException;
}
