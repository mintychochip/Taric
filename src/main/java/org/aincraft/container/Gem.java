package org.aincraft.container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IEffectContainer;
import org.aincraft.api.container.gem.IGem;
import org.aincraft.api.container.gem.IGem.IGemContainer;
import org.aincraft.api.container.gem.IGem.IGemContainerView;
import org.aincraft.api.container.gem.IItemContainerHolder;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.aincraft.util.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class Gem extends AbstractHolder<IGemContainer, IGemContainerView> implements IGem {

  static final NamespacedKey GEM_KEY = new NamespacedKey("taric", "gem");

  public Gem(ItemStack stack, IGemContainer container) {
    super(stack, container);
  }

  @Override
  public ISocketColor getSocketColor() {
    return container.getSocketColor();
  }

  private static final class View extends
      AbstractView<Container, IGemContainer, IGemContainerView> implements
      IGemContainerView {


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
    public ISocketColor getSocketColor() {
      return container.getSocketColor();
    }

    @Override
    public IGemEffect getEffect() {
      return container.getEffect();
    }

    @Override
    public int getRank() {
      return container.getRank();
    }
  }


  static final class Container extends AbstractEffectContainer<IGemContainerView> implements
      IGemContainer {

    @Nullable
    @Expose
    @SerializedName("effect")
    private IGemEffect effect;

    @Expose
    @SerializedName("rank")
    private int rank;

    @Expose
    @SerializedName("color")
    private final ISocketColor socketColor;

    Container(NamespacedKey containerKey, ISocketColor socketColor) {
      super(containerKey);
      this.socketColor = socketColor;
    }

    @Override
    public boolean setEffect(IGemEffect effect, int rank, boolean force) {
      if (!effect.getSocketColor().equals(this.socketColor) && !force) {
        return false;
      }
      this.effect = effect;
      this.rank = rank;
      return true;
    }

    @Override
    public void removeEffect(IGemEffect effect) {
      if (this.effect == null || rank == 0) {
        return;
      }
      if (effect.equals(this.effect)) {
        clear();
      }
    }

    @Override
    public void move(IItemContainerHolder<? extends IEffectContainer<?>, ?> holder) {
      if (effect == null || rank == 0) {
        return;
      }
      holder.editContainer(container -> {
        if (container.setEffect(effect, rank)) {
          this.clear();
        }
      });
    }

    @Override
    public ISocketColor getSocketColor() {
      return socketColor;
    }

    @Override
    public IGemEffect getEffect() {
      return effect;
    }

    @Override
    public int getRank(IGemEffect effect) {
      if (this.effect == null) {
        return 0;
      }
      return this.effect.equals(effect) ? rank : 0;
    }

    @Override
    public void clear() {
      this.effect = null;
      this.rank = 0;
    }

    @Override
    protected IGemContainerView buildView() {
      return new View(this);
    }

    @Override
    public int getRank() {
      return rank;
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
      ItemHolderFactory<IGem, IGemContainer, IGemContainerView> implements IGemFactory{

    @Override
    protected Class<? extends IGemContainer> getContainerImplClazz() {
      return Container.class;
    }

    @Override
    protected NamespacedKey getContainerKey() {
      return GEM_KEY;
    }

    @Override
    protected IGem create(ItemStack stack, IGemContainer container) {
      return new Gem(stack, container);
    }

    @Override
    public IGem create(Material material, ISocketColor socketColor) {
      return new Gem(new ItemStack(material), new Container(Gem.GEM_KEY, socketColor));
    }
  }
}
