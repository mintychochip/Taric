package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IContainerHolder;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IEffectContainerView;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
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

final class SocketGem extends
    AbstractGem<ISocketGemContainer, ISocketGemContainerView> implements
    ISocketGem {

  static final NamespacedKey GEM_KEY = new NamespacedKey("taric", "gem");

  public SocketGem(ItemStack stack, ISocketGemContainer container) {
    super(stack, container);
  }

  private static final class View extends
      AbstractGemView<SocketGemContainer, ISocketGemContainerView> implements
      ISocketGemContainerView {


    View(SocketGemContainer container) {
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
      ISocketColor socketColor = container.getColor();
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
      ISocketColor socketColor = container.getColor();
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
    public ISocketColor getColor() {
      return container.getColor();
    }
  }


  static final class SocketGemContainer extends
      AbstractGemContainer<ISocketGemContainerView> implements
      ISocketGemContainer {

    @Expose
    @SerializedName("color")
    private ISocketColor color;

    SocketGemContainer(NamespacedKey containerKey, ISocketColor color) {
      super(containerKey);
      this.color = color;
    }

    @Override
    public boolean canApplyEffect(IGemEffect effect, EffectInstanceMeta meta) {
      if (effect == null || meta.getRank() == 0) {
        return false;
      }
      ISocketColor socketColor = effect.getSocketColor();
      return this.color.equals(socketColor);
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
      super.applyEffect(effect, meta, force);
    }

    @Override
    public ISocketColor getColor() {
      return color;
    }

    @Override
    public void move(
        @NotNull IContainerHolder<? extends IEffectContainer<?>, ? extends IEffectContainerView> holder)
        throws IllegalArgumentException, NullPointerException, IllegalStateException {
      Preconditions.checkNotNull(holder);
      Preconditions.checkNotNull(effect);
      Preconditions.checkState(meta.getRank() > 0);
      holder.editContainer(container -> {
        container.applyEffect(effect, meta);
        this.clear();
      });
    }

    @Override
    public boolean canMerge(ISocketGem other) {
      if (effect == null || meta.getRank() == 0) {
        return false;
      }
      IGemContainerView view = other.getContainer();
      if (view == null) {
        return false;
      }
      IGemEffect viewEffect = view.getEffect();
      if (viewEffect == null) {
        return false;
      }
      if (!viewEffect.equals(effect) || view.getRank() != meta.getRank()) {
        return false;
      }
      return (meta.getRank() + 1) <= effect.getMaxRank();
    }

    @Override
    public void merge(ISocketGem other)
        throws IllegalArgumentException, IllegalStateException, NullPointerException {
      if (effect == null || meta == null) {
        throw new IllegalStateException("cannot merge a null effect");
      }
      Preconditions.checkNotNull(other);
      IGemContainerView view = other.getContainer();
      IGemEffect otherEffect = view.getEffect();
      int otherRank = view.getRank();
      Preconditions.checkNotNull(otherEffect, "cannot merge a null effect");
      Preconditions.checkArgument(otherRank > 0, "cannot merge a null effect");
      if (!otherEffect.equals(effect) || otherRank != meta.getRank()) {
        throw new IllegalArgumentException("other gem must have the same effect and rank");
      }
      //TODO: ensure cloning is proper
      int newRank = meta.getRank() + 1;
      JsonObject extra = meta.getExtra();
      EffectInstanceMeta instanceMeta = new EffectInstanceMeta(newRank);
      if (newRank > otherEffect.getMaxRank()) {
        throw new IllegalArgumentException(
            "failed to merge gems, the resulting combination would be over the max rank");
      }
      this.applyEffect(effect, instanceMeta);
      other.editContainer(container -> {
        container.removeEffect(effect);
      });
    }

    @Override
    protected ISocketGemContainerView buildView() {
      return new View(this);
    }
  }

  static final class SocketGemFactory extends
      ContainerHolderFactory<ISocketGem, ISocketGemContainer, ISocketGemContainerView> implements
      ISocketGemFactory {

    @Override
    protected NamespacedKey getContainerKey() {
      return GEM_KEY;
    }

    @Override
    protected ISocketGem create(ItemStack stack, ISocketGemContainer container) {
      return new SocketGem(stack, container);
    }

    @Override
    protected Class<? extends ISocketGemContainer> getContainerImplClazz() {
      return SocketGemContainer.class;
    }

    @Override
    public @NotNull ISocketGem create(Material material, ISocketColor socketColor) {
      return new SocketGem(new ItemStack(material),
          new SocketGemContainer(SocketGem.GEM_KEY, socketColor));
    }

    @Override
    public @NotNull ISocketGem create(Material material, IGemEffect effect, int rank,
        boolean force) {
      ISocketColor socketColor = effect.getSocketColor();
      ISocketGem gem = create(material, socketColor);
      EffectInstanceMeta meta = new EffectInstanceMeta(rank);
      gem.editContainer(container -> {
        container.applyEffect(effect, meta, force);
      });
      return gem;
    }

    @Override
    public @NotNull ISocketGem create(Material material, IGemEffect effect) {
      return create(material, effect, effect.getMaxRank(), false);
    }
  }

  @Override
  public @NotNull ISocketColor getColor() {
    return container.getColor();
  }
}
