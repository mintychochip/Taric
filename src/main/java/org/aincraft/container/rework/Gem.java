package org.aincraft.container.rework;

import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.aincraft.Taric;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.container.rework.IGem.IGemContainer;
import org.aincraft.container.rework.IGem.IGemContainerView;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class Gem extends AbstractHolder<IGemContainer, IGemContainerView> implements IGem {

  private static final NamespacedKey GEM_KEY = new NamespacedKey(Taric.getPlugin(), "gem");

  public Gem(ItemStack stack, IGemContainer container) {
    super(stack, container);
  }

  @Override
  public ISocketColor getSocketColor() {
    return container.getSocketColor();
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
      return null;
    }

    @Override
    protected ItemLore toItemLore() {
      return null;
    }

    @Override
    public int getRank(IGemEffect effect) {
      return container.getRank(effect);
    }

    @Override
    public ISocketColor getSocketColor() {
      return container.socketColor;
    }

    @Override
    public IGemEffect getEffect() {
      return container.effect;
    }
  }


  private static final class Container extends AbstractContainer<IGemContainerView> implements
      IGemContainer {

    private IGemEffect effect;
    private int rank;
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
    public ISocketColor getSocketColor() {
      return socketColor;
    }

    @Override
    public IGemEffect getEffect() {
      return effect;
    }

    @Override
    public void move(IEffectContainerHolder<IGemContainer, IGemContainerView> target) {
      target.editContainer(container -> {
        boolean b = container.setEffect(effect, rank);
        if (b) {
          clear();
        }
      });
    }

    @Override
    public int getRank(IGemEffect effect) {
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
  }
}
