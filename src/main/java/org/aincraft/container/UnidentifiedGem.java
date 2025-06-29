package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.api.container.EffectInstanceMeta;
import org.aincraft.api.container.IIdentificationTable;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.AppraisalState;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainer;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainerView;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.container.AbstractGem.AbstractGemContainer;
import org.aincraft.container.AbstractGem.AbstractGemView;
import org.aincraft.container.util.ExponentialRandomSelector;
import org.aincraft.effects.IGemEffect;
import org.aincraft.registry.IRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class UnidentifiedGem extends
    AbstractContainerHolder<IUnidentifiedGemContainer, IUnidentifiedGemContainerView> implements
    IUnidentifiedGem {

  static final NamespacedKey UNIDENTIFIED_GEM_CONTAINER_KEY = new NamespacedKey("taric",
      "precious");

  public UnidentifiedGem(ItemStack stack, IUnidentifiedGemContainer container) {
    super(stack, container);
  }

  private static final class UnidentifiedGemContainerView extends
      AbstractGemView<UnidentifiedGemContainer, IUnidentifiedGemContainerView> implements
      IUnidentifiedGemContainerView {

    UnidentifiedGemContainerView(UnidentifiedGemContainer unidentifiedGemContainer) {
      super(unidentifiedGemContainer);
    }

    @Override
    protected Component toItemTitle() {
      Component rarityLabel = Component.text(container.rarity.getName())
          .color(container.rarity.getTextColor());
      Component label = container.state == AppraisalState.GROUND ? Component.empty()
          .append(Component.text("Ground").color(NamedTextColor.GOLD)).append(Component.space())
          : container.state == AppraisalState.CLEANED ? Component.empty()
              .append(Component.text("Cleaned").color(NamedTextColor.GOLD))
              .append(Component.space()) : Component.empty();
      return label.append(Component.text("Unidentified Gem (")).append(rarityLabel)
          .append(Component.text(")")).color(NamedTextColor.DARK_GRAY)
          .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      builder.addLine(Component.empty());
      builder.addLine(Component.text("An unusually bright gem,").color(NamedTextColor.DARK_GRAY));
      builder.addLine(
          Component.text("its luster untouched by soil or time.").color(NamedTextColor.DARK_GRAY));
      builder.addLine(Component.empty().append(Component.text("Perhaps a "))
          .append(Component.text("blacksmith").color(NamedTextColor.GOLD))
          .append(Component.text(" would know")).color(NamedTextColor.DARK_GRAY));
      builder.addLine(
          Component.text("what lies beneath its shine.").color(NamedTextColor.DARK_GRAY));

      builder.addLine(Component.empty());
      return builder.build();
    }

    @Override
    public @NotNull IRarity getRarity() {
      return container.getRarity();
    }

    @Override
    public AppraisalState getState() {
      return container.getState();
    }
  }

  static final class UnidentifiedGemContainer extends
      AbstractGemContainer<IUnidentifiedGemContainerView> implements IUnidentifiedGemContainer {

    @Expose
    @NotNull
    @SerializedName("rarity")
    private IRarity rarity;

    @Expose
    @SerializedName("state")
    private AppraisalState state;

    UnidentifiedGemContainer(NamespacedKey containerKey, @NotNull IRarity rarity, IGemEffect effect,
        EffectInstanceMeta meta) {
      super(containerKey);
      this.rarity = rarity;
      this.state = AppraisalState.INITIAL;
      this.effect = effect;
      this.meta = meta;
    }

    @Override
    public @NotNull IRarity getRarity() {
      return rarity;
    }

    @Override
    public void setRarity(@NotNull IRarity rarity) {
      this.rarity = rarity;
    }

    @Override
    public AppraisalState getState() {
      return state;
    }

    @Override
    public void setState(AppraisalState state) {
      this.state = state;
    }

    @Override
    protected IUnidentifiedGemContainerView buildView() {
      return new UnidentifiedGemContainerView(this);
    }

    @Override
    public boolean canApplyEffect(IGemEffect effect, EffectInstanceMeta meta) {
      return effect == null || meta == null;
    }
  }

  static final class UnidentifiedGemFactory extends
      ContainerHolderFactory<IUnidentifiedGem, IUnidentifiedGemContainer, IUnidentifiedGemContainerView> implements
      IUnidentifiedGemFactory {

    private final IRegistry<IRarity> rarityRegistry;
    private final IRandomSelector<IRarity> raritySelector;
    private final IRegistry<IGemEffect> effectRegistry;
    private final IRegistry<IIdentificationTable> tableRegistry;

    @Inject
    UnidentifiedGemFactory(IRegistry<IRarity> rarityRegistry,
        @Named("rarity-selector") IRandomSelector<IRarity> raritySelector,
        IRegistry<IGemEffect> effectRegistry, IRegistry<IIdentificationTable> tableRegistry) {
      this.rarityRegistry = rarityRegistry;
      this.raritySelector = raritySelector;
      this.effectRegistry = effectRegistry;
      this.tableRegistry = tableRegistry;
    }

    @Override
    protected NamespacedKey getContainerKey() {
      return UNIDENTIFIED_GEM_CONTAINER_KEY;
    }

    @Override
    protected IUnidentifiedGem create(ItemStack stack, IUnidentifiedGemContainer container) {
      return new UnidentifiedGem(stack, container);
    }

    @Override
    protected Class<? extends IUnidentifiedGemContainer> getContainerImplClazz() {
      return UnidentifiedGemContainer.class;
    }

    @Override
    public IUnidentifiedGem create(@NotNull ItemStack stack, @NotNull IRarity rarity)
        throws IllegalArgumentException, NullPointerException {
      Preconditions.checkNotNull(rarity);
      Preconditions.checkArgument(rarityRegistry.isRegistered(rarity));
      Preconditions.checkArgument(tableRegistry.isRegistered(rarity.key()));
      IIdentificationTable identificationTable = tableRegistry.get(rarity.key());
      IRarity selected = identificationTable.getRandom(Taric.getRandom());
      List<IGemEffect> effects = effectRegistry.stream().filter(e -> e.getRarity().equals(selected))
          .toList();
      if (!effects.isEmpty()) {
        int index = Taric.getRandom().nextInt(effects.size());
        IGemEffect effect = effects.get(index);
        IRandomSelector<Integer> selector = createRankSelector(effect.getMaxRank(),
            selected.getDecayRate());
        UnidentifiedGemContainer container = new UnidentifiedGemContainer(
            UNIDENTIFIED_GEM_CONTAINER_KEY, rarity, effect,
            new EffectInstanceMeta(selector.getRandom(Taric.getRandom())));
        return new UnidentifiedGem(stack, container);
      }
      int index = Taric.getRandom().nextInt(effectRegistry.size());
      IGemEffect effect = effectRegistry.get(index);
      IRandomSelector<Integer> selector = createRankSelector(effect.getMaxRank(),
          selected.getDecayRate());
      UnidentifiedGemContainer container = new UnidentifiedGemContainer(
          UNIDENTIFIED_GEM_CONTAINER_KEY, rarity, effect,
          new EffectInstanceMeta(selector.getRandom(Taric.getRandom())));
      return new UnidentifiedGem(stack, container);
    }


    @Override
    public IUnidentifiedGem create(@NotNull ItemStack stack)
        throws IllegalArgumentException, NullPointerException {
      IRarity rarity = raritySelector.getRandom(Taric.getRandom());
      return create(stack, rarity);
    }

    private static IRandomSelector<Integer> createRankSelector(int maxRank, double decayRate) {
      ExponentialRandomSelector<Integer> selector = new ExponentialRandomSelector<>(decayRate);
      for (int i = 1; i <= maxRank; ++i) {
        selector.add(i);
      }
      return selector;
    }
  }

  @Override
  public @NotNull ItemStack getStack() {
    return stack;
  }
}
