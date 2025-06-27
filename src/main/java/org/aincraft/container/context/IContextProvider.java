package org.aincraft.container.context;

import org.bukkit.event.Event;

public interface IContextProvider<C, E extends Event> {

  C create(E handle);
}
