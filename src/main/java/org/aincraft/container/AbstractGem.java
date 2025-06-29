package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class AbstractGem<C extends IGemContainer<V>, V extends IGemContainerView> extends
    AbstractContainerHolder<C, V> {

  public AbstractGem(ItemStack stack, C container) {
    super(stack, container);
  }

  static class AbstractGemView<CImpl extends AbstractGemContainer<V>, V extends IGemContainerView> extends
      AbstractView<CImpl, V> implements IGemContainerView {

    AbstractGemView(CImpl container) {
      super(container);
    }

    @Override
    public @NotNull IGemEffect getEffect() {
      return container.getEffect();
    }

    @Override
    public int getRank() {
      return container.getRank();
    }

    @Override
    public int getRank(IGemEffect effect) {
      return container.getRank(effect);
    }
  }

  static abstract class AbstractGemContainer<V extends IGemContainerView> extends
      AbstractContainer<V> implements IGemContainer<V> {

    @Expose
    @SerializedName("effect")
    protected IGemEffect effect;

    @Expose
    @SerializedName("meta")
    protected EffectInstanceMeta meta;

    AbstractGemContainer(NamespacedKey containerKey) {
      super(containerKey);
    }

    @Override
    public void applyEffect(@NotNull IGemEffect effect, EffectInstanceMeta meta, boolean force)
        throws IllegalArgumentException, NullPointerException {
      this.effect = effect;
      this.meta = meta.copy();
    }

    @Override
    public void removeEffect(@NotNull IGemEffect effect)
        throws IllegalArgumentException, NullPointerException {
      Preconditions.checkNotNull(effect);
      Preconditions.checkNotNull(this.effect);
      if (!this.effect.equals(effect)) {
        throw new IllegalArgumentException(
            "effect: %s must be the same as the effect being removed: %s".formatted(
                effect.getName(), this.effect.getName()));
      }
      clear();
    }

    @Override
    public boolean hasEffect(@Nullable IGemEffect effect) {
      if (effect == null || this.effect == null) {
        return false;
      }
      return this.effect.equals(effect);
    }

    @Override
    public int getRank(IGemEffect effect) {
      if (effect == null || effect != this.effect) {
        return 0;
      }
      return meta.getRank();
    }

    @Override
    public void clear() {
      this.effect = null;
      this.meta = null;
    }

    @Override
    public @NotNull IGemEffect getEffect() {
      return effect;
    }

    @Override
    public int getRank() {
      return meta != null ? meta.getRank() : 0;
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getClass().getSimpleName())
          .append("{effect=")
          .append(effect != null ? effect.getName() : "null")
          .append(", meta=")
          .append(meta)
          .append('}')
          .toString();
    }
  }
}
