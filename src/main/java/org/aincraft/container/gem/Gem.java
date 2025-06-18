//package org.aincraft.container.gem;
//
//import com.google.gson.annotations.SerializedName;
//import io.papermc.paper.datacomponent.item.ItemLore;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//import net.kyori.adventure.text.Component;
//import net.kyori.adventure.text.format.TextColor;
//import net.kyori.adventure.text.format.TextDecoration;
//import org.aincraft.Taric;
//import org.aincraft.container.Rarity;
//import org.aincraft.effects.IGemEffect;
//import org.aincraft.util.Roman;
//import org.bukkit.Material;
//import org.bukkit.NamespacedKey;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class Gem implements IEffectContainerHolder {
//
//  private static final NamespacedKey GEM_KEY = new NamespacedKey(Taric.getPlugin(), "gem");
//  private static final Supplier<Map<String, Integer>> MAP_SUPPLIER = () ->
//      new TreeMap<>((a, b) -> {
//        IGemEffect effectA = Taric.getEffects().get(a);
//        IGemEffect effectB = Taric.getEffects().get(b);
//        if (effectA == null && effectB == null) {
//          return 0;
//        }
//        if (effectA == null) {
//          return 1;
//        }
//        if (effectB == null) {
//          return -1;
//        }
//        int rarityComparison = effectB.getRarity().compareTo(effectA.getRarity());
//        if (rarityComparison != 0) {
//          return rarityComparison;
//        }
//        return effectA.getName().compareTo(effectB.getName());
//      });
//
//  private final ItemStack stack;
//
//  private final IEffectContainer container;
//
//  @Nullable
//  public static Gem fromIfExists(ItemStack stack) {
//    Material material = stack.getType();
//    if (material.isAir() || !IEffectContainer.hasContainer(stack)) {
//      return null;
//    }
//    IEffectContainer effectContainer = IEffectContainer.from(stack, Container.class);
//    return new Gem(stack, effectContainer);
//  }
//
//  @NotNull
//  public static Gem from(ItemStack stack, Callable<? extends Gem> loader)
//      throws ExecutionException {
//    if (!IEffectContainer.hasContainer(stack)) {
//      try {
//        return loader.call();
//      } catch (Exception e) {
//        throw new ExecutionException(e);
//      }
//    }
//    Gem gem = fromIfExists(stack);
//    assert gem != null;
//    return gem;
//  }
//
//  public Gem(ItemStack stack, IEffectContainer container) {
//    this.stack = stack;
//    this.container = container;
//    container.update(stack);
//  }
//
//  public ItemStack getStack() {
//    return stack;
//  }
//
//  @Nullable
//  public static Gem create(ItemStack stack) {
//    if (IEffectContainer.hasContainer(stack)) {
//      return null;
//    }
//    return new Gem(stack, new Container(MAP_SUPPLIER.get()));
//  }
//
//  public static IGemBuilder.EffectStep builder(Material material) {
//    return new GemBuilder(new ItemStack(material), new Container(MAP_SUPPLIER.get()));
//  }
//
//  @Override
//  public void editContainer(Consumer<IEffectContainer> containerConsumer) {
//    containerConsumer.accept(container);
//    container.update(stack);
//  }
//
//  @Override
//  public IEffectContainerView getEffectContainerView() {
//    return container.getView();
//  }
//
//  static final class GemBuilder implements IGemBuilder.FinalStep, IGemBuilder.EffectStep {
//
//    private final ItemStack itemStack;
//    private final IEffectContainer container;
//
//    GemBuilder(ItemStack itemStack,
//        IEffectContainer container) {
//      this.itemStack = itemStack;
//      this.container = container;
//    }
//
//    @Override
//    public Gem build() {
//      return new Gem(itemStack, container);
//    }
//
//    @Override
//    public IGemBuilder.FinalStep effect(IGemEffect effect, int rank) {
//      container.addEffect(effect, rank);
//      return this;
//    }
//
//    @Override
//    public IGemBuilder.FinalStep effect(IGemEffect effect, int rank, boolean force) {
//      container.addEffect(effect, rank, force);
//      return this;
//    }
//  }
//
//
//  private static final class View extends AbstractEffectContainerView {
//
//
//    View(Map<String, Integer> effects, NamespacedKey key) {
//      super(effects, key);
//    }
//
//    @Override
//    protected ItemLore effectsToLore() {
//      ItemLore.Builder builder = ItemLore.lore();
//      for (Entry<String, Integer> entry : effects.entrySet()) {
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
//
//    @Override
//    public NamespacedKey getKey() {
//      return new NamespacedKey(Taric.getPlugin(), "gem");
//    }
//  }
//
//  private static final class Container extends AbstractEffectContainer {
//
//    @SerializedName("effects")
//    private final Map<String, Integer> effects;
//
//    private Container(Map<String, Integer> effects) {
//      this.effects = effects;
//    }
//
//    @Override
//    protected Map<String, Integer> delegate() {
//      return effects;
//    }
//
//    @Override
//    protected IEffectContainerView buildView() {
//      return new View(effects, GEM_KEY);
//    }
//
//    @SuppressWarnings("CloneDoesntCallSuperClone")
//    @Override
//    public IEffectContainer clone() {
//      return null;
//    }
//  }
//
//}
