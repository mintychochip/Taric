package org.aincraft.container.gem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import org.aincraft.Taric;
import org.aincraft.api.container.gem.IEffectContainerHolder;
import org.aincraft.api.container.gem.IGem;
import org.aincraft.api.container.gem.IGem.IContainer;
import org.aincraft.api.container.gem.IGem.IView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Gem extends
    AbstractEffectContainerHolder<IView, IContainer> implements IGem {

  private static final NamespacedKey GEM_KEY = new NamespacedKey(Taric.getPlugin(), "gem");
  private static final Supplier<Map<Key, Integer>> MAP_SUPPLIER = () ->
      new TreeMap<>((a, b) -> {
        IGemEffect effectA = Taric.getEffects().get(a);
        IGemEffect effectB = Taric.getEffects().get(b);
        if (effectA == null && effectB == null) {
          return 0;
        }
        if (effectA == null) {
          return 1;
        }
        if (effectB == null) {
          return -1;
        }
        int rarityComparison = effectB.getRarity().compareTo(effectA.getRarity());
        if (rarityComparison != 0) {
          return rarityComparison;
        }
        return effectA.getName().compareTo(effectB.getName());
      });

  Gem(ItemStack stack, IContainer container) {
    super(stack, container);
  }


  @Nullable
  public static Gem fromIfExists(ItemStack stack) {
    return GemItemFactory.holderFromIfExists(stack, GEM_KEY,
        Gem.Container.class, container -> new Gem(stack, container));
  }

  @NotNull
  public static Gem from(ItemStack stack, Callable<? extends Gem> loader)
      throws ExecutionException {
    return GemItemFactory.holderFrom(stack, GEM_KEY, loader,
        Gem.Container.class,
        container -> new Gem(stack, container));
  }

  @Nullable
  public static Gem create(ItemStack stack) {
    if (GemItemFactory.hasContainer(GEM_KEY, stack)) {
      return null;
    }
    return new Gem(stack, new Container(MAP_SUPPLIER.get()));
  }

  @NotNull
  public static IEffectContainerHolder.Builder<IGem,IContainer,IView> builder(Material material) {
    return new Builder(new ItemStack(material),new Container(MAP_SUPPLIER.get()));
  }

  private static final class Builder extends
      AbstractEffectContainerHolder.Builder<IGem, IContainer, IView> {


    Builder(ItemStack stack, IContainer container) {
      super(stack, container);
    }

    @Override
    public IGem build() {
      return new Gem(stack, container);
    }
  }

  private static final class View extends
      AbstractEffectContainerView<Container, IContainer, IView> implements IView {

    View(Map<Key, Integer> effects, NamespacedKey key, Container container) {
      super(effects, key, container);
    }

//    @Override
//    protected ItemLore effectsToLore() {
//      ItemLore.Builder builder = ItemLore.lore();
//      for (Entry<Key, Integer> entry : effects.entrySet()) {
//        IGemEffect effect = Taric.getEffects().get(entry.getKey());
//        if (effect == null) {
//          continue;
//        }
//        Rarity rarity = effect.getRarity();
//        String name = effect.getName();
//        TextColor color = TextColor.color(rarity.getColor());
//        Component styledName = Component.text(name).color(color);
//        Component roman = Component.text(Roman.fromInteger(entry.getValue()))
//            .color(color);
//        Component label = Component.empty().append(styledName).append(Component.space())
//            .append(roman).decoration(
//                TextDecoration.ITALIC, false);
//        builder.addLine(label);
//      }
//      return builder.build();
//    }
  }

  private static final class Container extends AbstractEffectContainer<IContainer, IView> implements
      IContainer {

    @Expose
    @SerializedName("effects")
    private final Map<Key, Integer> effects;

    private Container(Map<Key, Integer> effects) {
      this.effects = effects;
    }

    @Override
    protected Map<Key, Integer> delegate() {
      return effects;
    }

    @Override
    protected IView buildView() {
      return new View(effects, GEM_KEY, this);
    }

    @Override
    public IContainer clone() {
      Container container = new Container(MAP_SUPPLIER.get());
      this.copy(container, true);
      return container;
    }
  }

}
