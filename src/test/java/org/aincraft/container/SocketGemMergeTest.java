package org.aincraft.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.SocketGem.SocketGemContainer;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

/**
 * Exercises shipped merge meta cloning: {@link SocketGemContainer#mergedMeta} and
 * {@link SocketGemContainer#merge} without constructing Bukkit ItemStacks (no live server).
 */
class SocketGemMergeTest {

  @Test
  void mergedMeta_incrementsRankAndDeepCopiesExtra() {
    EffectInstanceMeta base = new EffectInstanceMeta(2);
    base.getExtra().addProperty("seed", 7);

    EffectInstanceMeta merged = SocketGemContainer.mergedMeta(base);

    assertEquals(3, merged.getRank());
    assertEquals(2, base.getRank());
    assertEquals(7, merged.getExtra().get("seed").getAsInt());
    assertNotSame(base.getExtra(), merged.getExtra());

    merged.getExtra().addProperty("mutated", true);
    assertFalse(base.getExtra().has("mutated"));
  }

  @Test
  void merge_onContainer_preservesExtraAndIncrementsRank() {
    ISocketColor color = mock(ISocketColor.class);
    IGemEffect effect = mock(IGemEffect.class);
    when(effect.getMaxRank()).thenReturn(5);
    when(effect.getSocketColor()).thenReturn(color);

    NamespacedKey key = new NamespacedKey("taric", "gem");
    SocketGemContainer primary = new SocketGemContainer(key, color);
    SocketGemContainer secondary = new SocketGemContainer(key, color);

    // Minimal gem holders so merge can call other.getContainer() / editContainer
    SocketGem primaryGem = new SocketGem(null, primary);
    SocketGem secondaryGem = new SocketGem(null, secondary);

    EffectInstanceMeta primaryMeta = new EffectInstanceMeta(2);
    primaryMeta.getExtra().addProperty("charge", 3);
    EffectInstanceMeta secondaryMeta = new EffectInstanceMeta(2);

    primary.applyEffect(effect, primaryMeta, true);
    secondary.applyEffect(effect, secondaryMeta, true);

    assertTrue(primary.canMerge(secondaryGem));
    primary.merge(secondaryGem);

    assertEquals(3, primary.getRank());
    assertEquals(effect, primary.getEffect());
    JsonObject extra = primary.meta.getExtra();
    assertEquals(3, extra.get("charge").getAsInt());

    extra.addProperty("after-merge", true);
    assertFalse(primaryMeta.getExtra().has("after-merge"));

    // Secondary was cleared by merge
    assertNull(secondary.effect);
  }
}
