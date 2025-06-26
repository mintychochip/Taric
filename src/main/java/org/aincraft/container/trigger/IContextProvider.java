package org.aincraft.container.trigger;

public interface IContextProvider<C, H> {

  C create(H handle);
}
