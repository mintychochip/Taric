package org.aincraft.container.context;

import com.google.inject.Provider;

public final class DispatchProvider implements Provider<IDispatch> {

  @Override
  public IDispatch get() {
    return new Dispatch();
  }
}
