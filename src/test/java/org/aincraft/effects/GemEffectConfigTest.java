package org.aincraft.effects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.aincraft.api.config.IConfiguration;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

/**
 * Drives shipped {@link GemEffectConfig} path construction (helpers no longer live on IGemEffect).
 */
class GemEffectConfigTest {

  @Test
  void loadDouble_readsSettingsPathUnderEffectKey() {
    IGemEffect effect = mock(IGemEffect.class);
    when(effect.key()).thenReturn(new NamespacedKey("taric", "vampirism"));

    IConfiguration config = mock(IConfiguration.class);
    when(config.contains("vampirism.settings.factor")).thenReturn(true);
    when(config.getDouble("vampirism.settings.factor")).thenReturn(0.35);

    assertEquals(0.35, GemEffectConfig.loadDouble(effect, config, "factor", 1.0));
  }

  @Test
  void loadInt_readsSettingsPathUnderEffectKey() {
    IGemEffect effect = mock(IGemEffect.class);
    when(effect.key()).thenReturn(new NamespacedKey("taric", "insight"));

    IConfiguration config = mock(IConfiguration.class);
    when(config.contains("insight.settings.orbs-min")).thenReturn(true);
    when(config.getInt("insight.settings.orbs-min")).thenReturn(2);

    assertEquals(2, GemEffectConfig.loadInt(effect, config, "orbs-min", 1));
  }

  @Test
  void loadStringList_returnsConfiguredList() {
    IGemEffect effect = mock(IGemEffect.class);
    when(effect.key()).thenReturn(new NamespacedKey("taric", "scavenge"));

    IConfiguration config = mock(IConfiguration.class);
    when(config.contains("scavenge.settings.black-list")).thenReturn(true);
    when(config.getStringList("scavenge.settings.black-list")).thenReturn(
        List.of("bedrock", "barrier"));

    assertEquals(List.of("bedrock", "barrier"),
        GemEffectConfig.loadStringList(effect, config, "black-list", List.of()));
  }
}
