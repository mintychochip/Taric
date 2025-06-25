package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.api.container.gem.IItemContainerHolder;
import org.aincraft.api.container.gem.ISocketGem;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemContainer;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.aincraft.util.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SocketGem extends
    AbstractHolder<ISocketGemContainer, ISocketGemContainerView> implements
    ISocketGem {

  static final NamespacedKey GEM_KEY = new NamespacedKey("taric", "gem");

  public SocketGem(ItemStack stack, ISocketGemContainer container) {
    super(stack, container);
  }

  private static final class View extends
      AbstractView<Container, ISocketGemContainer, ISocketGemContainerView> implements
      ISocketGemContainerView {


    View(Container container) {
      super(container);
    }

    @Override
    protected Component toItemTitle() {
      Component title = Component.empty().append(Component.text("Gem"))
          .decoration(TextDecoration.ITALIC, false);
      IGemEffect effect = container.getEffect();
      if (effect != null) {
        title = title.append(Component.text(" of "))
            .append(Component.text(Utils.toTitleCase(effect.getAdjective())));
        int rank = container.getRank();
        int maxRank = effect.getMaxRank();
        title = Component.empty().append(Component.text(determineGemPrefix(rank, maxRank)))
            .append(Component.space())
            .append(title);
      }
      ISocketColor socketColor = container.getSocketColor();
      if (socketColor != null) {
        title = title.color(socketColor.getTextColor());
      }
      return title;
    }

    private static String determineGemPrefix(int rank, int maxRank) {
      if (maxRank <= 0) {
        return "";
      }
      if (maxRank == 1) {
        return "Greater";
      }

      double percent = (double) rank / maxRank;

      if (percent < 0.5) {
        return "Lesser";
      } else if (percent < 0.8) {
        return "Major";
      } else {
        return "Greater";
      }
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      IGemEffect effect = container.getEffect();
      if (effect != null) {
        Component roman = Component.text(Roman.fromInteger(container.getRank()));
        Component label = Component.empty().append(Component.text(effect.getName()))
            .append(Component.space()).append(roman).decoration(TextDecoration.ITALIC, false)
            .color(NamedTextColor.DARK_GRAY);
        builder.addLine(label);
      }
      builder.addLine(Component.empty());
      ISocketColor socketColor = container.getSocketColor();
      if (socketColor != null) {
        Component colorLabel = Component.empty()
            .append(Component.text(Utils.toTitleCase(socketColor.getName())))
            .decoration(TextDecoration.ITALIC, false).color(socketColor.getTextColor());
        builder.addLine(colorLabel);
        builder.addLine(Component.empty());
      }

      return builder.build();
    }

    @Override
    public int getRank(IGemEffect effect) {
      return container.getRank(effect);
    }

    @Override
    public @NotNull ISocketColor getSocketColor() {
      return container.getSocketColor();
    }

    @Override
    public @Nullable IGemEffect getEffect() {
      return container.getEffect();
    }

    @Override
    public int getRank() {
      return container.getRank();
    }
  }

  static final class Container extends AbstractEffectContainer<ISocketGemContainerView> implements
      ISocketGemContainer {

    @Expose
    @SerializedName("color")
    private final ISocketColor socketColor;

    @Nullable
    @Expose
    @SerializedName("effect")
    private IGemEffect effect;
    @Expose
    @SerializedName("rank")
    private int rank;

    Container(NamespacedKey containerKey, ISocketColor socketColor) {
      super(containerKey);
      this.socketColor = socketColor;
    }

    @Override
    public boolean canApplyEffect(IGemEffect effect, int rank) {
      if (effect == null || rank == 0) {
        return false;
      }
      ISocketColor socketColor = effect.getSocketColor();
      return this.socketColor.equals(socketColor);
    }

    @Override
    public void applyEffect(@NotNull IGemEffect effect, int rank, boolean force)
        throws IllegalArgumentException, NullPointerException {
      if (!force) {
        Preconditions.checkNotNull(effect, "effect cannot be null");
        Preconditions.checkArgument(rank > 0, "rank: %d must be greater than 0".formatted(rank));
        Preconditions.checkArgument(rank <= effect.getMaxRank(),
            "rank: %d cannot be greater than max rank: %d".formatted(rank, effect.getMaxRank()));
        Preconditions.checkArgument(effect.getSocketColor().equals(socketColor),
            "gem colors must be the same");
      }
      this.effect = effect;
      this.rank = rank;
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
    public void clear() {
      this.effect = null;
      this.rank = 0;
    }

    @Override
    public @NotNull ISocketColor getSocketColor() {
      return socketColor;
    }

    @Override
    public @Nullable IGemEffect getEffect() {
      return effect;
    }

    @Override
    public int getRank() {
      return rank;
    }

    @Override
    public void move(
        @NotNull IItemContainerHolder<? extends IEffectContainer<?>, ? extends IEffectContainerView> holder)
        throws IllegalArgumentException, NullPointerException, IllegalStateException {
      Preconditions.checkNotNull(holder);
      Preconditions.checkNotNull(effect);
      Preconditions.checkState(rank > 0);
      holder.editContainer(container -> {
        container.applyEffect(effect, rank);
        this.clear();
      });
    }

    @Override
    public boolean canMerge(ISocketGem other) {
      if (effect == null || rank == 0) {
        return false;
      }
      ISocketGemContainerView view = other.getContainer();
      if (!view.getEffect().equals(effect) || view.getRank() != rank) {
        return false;
      }
      return (rank + 1) <= effect.getMaxRank();
    }

    @Override
    public void merge(ISocketGem other)
        throws IllegalArgumentException, IllegalStateException, NullPointerException {
      if (effect == null || rank == 0) {
        throw new IllegalStateException("cannot merge a null effect");
      }
      Preconditions.checkNotNull(other);
      ISocketGemContainerView otherView = other.getContainer();
      IGemEffect otherEffect = otherView.getEffect();
      int otherRank = otherView.getRank();
      Preconditions.checkNotNull(otherEffect, "cannot merge a null effect");
      Preconditions.checkArgument(otherRank > 0, "cannot merge a null effect");
      if (!otherEffect.equals(effect) || otherRank != rank) {
        throw new IllegalArgumentException("other gem must have the same effect and rank");
      }
      int newRank = rank + 1;
      if (newRank > otherEffect.getMaxRank()) {
        throw new IllegalArgumentException(
            "failed to merge gems, the resulting combination would be over the max rank");
      }
      this.applyEffect(effect, newRank);
      other.editContainer(container -> {
        container.removeEffect(effect);
      });
    }

    @Override
    public int getRank(IGemEffect effect) {
      if (this.effect == null) {
        return 0;
      }
      return this.effect.equals(effect) ? rank : 0;
    }

    @Override
    protected ISocketGemContainerView buildView() {
      return new View(this);
    }

    @Override
    public String toString() {
      return new StringBuilder(this.getClass().getSimpleName())
          .append("{effect=")
          .append(effect != null ? effect.getName() : "null")
          .append(", rank=")
          .append(rank)
          .append(", socketColor=")
          .append(socketColor)
          .append('}')
          .toString();
    }
  }

  static final class Factory extends
      ItemHolderFactory<ISocketGem, ISocketGemContainer, ISocketGemContainerView> implements
      ISocketGemFactory {

    @Override
    protected Class<? extends ISocketGemContainer> getContainerImplClazz() {
      return Container.class;
    }

    @Override
    protected NamespacedKey getContainerKey() {
      return GEM_KEY;
    }

    @Override
    protected ISocketGem create(ItemStack stack, ISocketGemContainer container) {
      return new SocketGem(stack, container);
    }

    @Override
    public @NotNull ISocketGem create(Material material, ISocketColor socketColor) {
      return new SocketGem(new ItemStack(material), new Container(SocketGem.GEM_KEY, socketColor));
    }

    @Override
    public @NotNull ISocketGem create(Material material, IGemEffect effect, int rank,
        boolean force) {
      ISocketColor socketColor = effect.getSocketColor();
      ISocketGem gem = create(material, socketColor);
      gem.editContainer(container -> {
        container.applyEffect(effect, rank, force);
      });
      return gem;
    }
  }

  @Override
  public @NotNull ISocketColor getSocketColor() {
    return container.getSocketColor();
  }
}
