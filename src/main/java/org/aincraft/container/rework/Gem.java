package org.aincraft.container.rework;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGem.IGemContainer;
import org.aincraft.container.rework.IGem.IGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.aincraft.util.Roman;
import org.aincraft.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class Gem extends AbstractHolder<IGemContainer, IGemContainerView> implements IGem {

  private static final NamespacedKey GEM_KEY = new NamespacedKey("taric", "gem");

  public Gem(ItemStack stack, IGemContainer container) {
    super(stack, container);
  }

  @Override
  public ISocketColor getSocketColor() {
    return container.getSocketColor();
  }


  @Nullable
  public static IGem fromIfExists(ItemStack stack) {
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    if (!pdc.has(GEM_KEY)) {
      return null;
    }
    String string = pdc.get(GEM_KEY, PersistentDataType.STRING);
    Container container = Taric.getGson().fromJson(string, Container.class);
    return new Gem(stack, container);
  }

  public static IGem create(Material material, ISocketColor socketColor) {
    return new Gem(new ItemStack(material), new Container(GEM_KEY, socketColor));
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


  private static final class Container extends AbstractContainer<IGemContainerView> implements
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

    }

    @Override
    public void move(IEffectContainerHolder<?, ?> holder) {
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
      Bukkit.broadcastMessage("called clear");
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
}
