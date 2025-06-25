package org.aincraft.api.container.gem;

import org.aincraft.api.container.IRarity;
import org.bukkit.entity.Player;

public interface IGemIdentifier {

  ISocketGem identify(IPreciousGem unidentified, Player identifier) throws IllegalArgumentException;

  boolean canIdentify(IRarity rarity, Player identifier);

}
