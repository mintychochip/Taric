package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.ISocketColor;
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

  static abstract class AbstractGemContainer<V extends IGemContainerView> extends
      AbstractContainer<V> implements IGemContainer<V> {

    @Expose
    @SerializedName("color")
    protected final ISocketColor color;

    @Expose
    @SerializedName("effect")
    protected IGemEffect effect;

    @Expose
    @SerializedName("meta")
    protected EffectInstanceMeta meta;

    AbstractGemContainer(NamespacedKey containerKey, ISocketColor color) {
      super(containerKey);
      this.color = color;
    }

    static class AbstractGemView<CImpl extends AbstractGemContainer<V>, V extends IGemContainerView> extends
        AbstractView<CImpl, V> implements IGemContainerView {

      AbstractGemView(CImpl container) {
        super(container);
      }

      @Override
      public @NotNull ISocketColor getColor() {
        return container.getColor();
      }

      @Override
      public IGemEffect getEffect() {
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

    @Override
    public boolean canApplyEffect(IGemEffect effect, EffectInstanceMeta meta) {
      if (effect == null || meta.getRank() == 0) {
        return false;
      }
      ISocketColor color = effect.getSocketColor();
      return color.equals(this.color);
    }

    @Override
    public void applyEffect(@NotNull IGemEffect effect, EffectInstanceMeta meta, boolean force)
        throws IllegalArgumentException, NullPointerException {
      if (!force) {
        Preconditions.checkNotNull(effect, "effect cannot be null");
        Preconditions.checkArgument(meta.getRank() > 0,
            "rank: %d must be greater than 0".formatted(meta.getRank()));
        Preconditions.checkArgument(meta.getRank() <= effect.getMaxRank(),
            "rank: %d cannot be greater than max rank: %d".formatted(meta.getRank(),
                effect.getMaxRank()));
        Preconditions.checkArgument(effect.getSocketColor().equals(color),
            "gem colors must be the same");
      }
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
    public @NotNull ISocketColor getColor() {
      return color;
    }

    @Override
    public IGemEffect getEffect() {
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
          .append(", socketColor=")
          .append(color)
          .append('}')
          .toString();
    }
  }
}
