package org.aincraft.api.container;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.registry.IRegistry;

public final class SocketColors {

  public static ISocketColor RED;
  public static ISocketColor ORANGE;
  public static ISocketColor YELLOW;
  public static ISocketColor GREEN;
  public static ISocketColor BLUE;

  private static boolean initialized = false;

  public static void initialize(IRegistry<ISocketColor> registry) {
    if (initialized) {
      throw new IllegalStateException("colors are initialized");
    }
    initialized = true;
    RED = registry.get(Key.key("taric:red"));
    ORANGE = registry.get(Key.key("taric:orange"));
    YELLOW = registry.get(Key.key("taric:yellow"));
    GREEN = registry.get(Key.key("taric:green"));
    BLUE = registry.get(Key.key("taric:blue"));
  }

  public static List<ISocketColor> values() {
    return List.of(RED, ORANGE, YELLOW, GREEN, BLUE);
  }
}
