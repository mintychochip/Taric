package org.aincraft.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class SharedRegistryTest {

  private record NamedEntry(@NotNull Key key) implements Keyed {}

  @Test
  void register_get_size_and_isRegistered() {
    SharedRegistry<NamedEntry> registry = new SharedRegistry<>();
    NamedEntry blink = new NamedEntry(Key.key("taric", "blink"));
    NamedEntry crush = new NamedEntry(Key.key("taric", "crush"));

    assertEquals(0, registry.size());
    assertFalse(registry.isRegistered(blink.key()));
    assertFalse(registry.isRegistered(blink));

    registry.register(blink);
    registry.register(crush);

    assertEquals(2, registry.size());
    assertTrue(registry.isRegistered(blink.key()));
    assertTrue(registry.isRegistered(blink));
    assertTrue(registry.isRegistered(crush.key()));
    assertSame(blink, registry.get(blink.key()));
    assertSame(blink, registry.get(blink));
    assertSame(crush, registry.get(Key.key("taric", "crush")));
    assertNull(registry.get(Key.key("taric", "missing")));
  }

  @Test
  void register_returnsSameRegistryForChaining() {
    SharedRegistry<NamedEntry> registry = new SharedRegistry<>();
    NamedEntry entry = new NamedEntry(Key.key("taric", "flare"));

    assertSame(registry, registry.register(entry));
  }

  @Test
  void values_containsRegisteredObjects() {
    SharedRegistry<NamedEntry> registry = new SharedRegistry<>();
    NamedEntry entry = new NamedEntry(Key.key("taric", "vorpal"));
    registry.register(entry);

    assertEquals(1, registry.values().size());
    assertTrue(registry.values().contains(entry));
  }
}
