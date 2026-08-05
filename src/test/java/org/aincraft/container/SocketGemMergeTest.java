package org.aincraft.container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.container.SocketGem.SocketGemContainer;
import org.aincraft.container.SocketGem.SocketGemFactory;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

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
  void merge_usesClonedMetaWithIncrementedRank() {
    ISocketColor color = mock(ISocketColor.class);
    IGemEffect effect = mock(IGemEffect.class);
    when(effect.getMaxRank()).thenReturn(5);
    when(effect.getSocketColor()).thenReturn(color);

    SocketGemFactory factory = new SocketGemFactory();
    ISocketGem primary = factory.create(Material.EMERALD, color);
    ISocketGem secondary = factory.create(Material.EMERALD, color);

    EffectInstanceMeta primaryMeta = new EffectInstanceMeta(2);
    primaryMeta.getExtra().addProperty("charge", 3);
    EffectInstanceMeta secondaryMeta = new EffectInstanceMeta(2);

    primary.editContainer(c -> c.applyEffect(effect, primaryMeta, true));
    secondary.editContainer(c -> c.applyEffect(effect, secondaryMeta, true));

    assertTrue(primary.getContainer().canMerge(secondary));
    primary.editContainer(c -> c.merge(secondary));

    assertEquals(3, primary.getContainer().getRank());
    assertEquals(effect, primary.getContainer().getEffect());

    // Same-package access to protected meta field on AbstractGemContainer
    SocketGemContainer container = (SocketGemContainer) readContainer(primary);
    JsonObject extra = container.meta.getExtra();
    assertEquals(3, extra.get("charge").getAsInt());
    // Mutating stored extra must not rewrite the original meta object we built
    extra.addProperty("after-merge", true);
    assertFalse(primaryMeta.getExtra().has("after-merge"));
  }

  /** Reach the concrete container held by the gem (package-private factory types). */
  private static SocketGemContainer readContainer(ISocketGem gem) {
    final SocketGemContainer[] box = new SocketGemContainer[1];
    gem.editContainer(c -> box[0] = (SocketGemContainer) c);
    return box[0];
  }
}
