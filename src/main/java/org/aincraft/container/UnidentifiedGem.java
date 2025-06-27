package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.AppraisalState;
import org.aincraft.api.container.gem.IUnidentifiedGem;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainer;
import org.aincraft.api.container.gem.IUnidentifiedGem.IUnidentifiedGemContainerView;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.container.AbstractGem.AbstractGemContainer;
import org.aincraft.container.AbstractGem.AbstractGemContainer.AbstractGemView;
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
      Component label =
          container.state == AppraisalState.GROUND ? Component.empty().append(
              Component.text("Ground").color(NamedTextColor.GOLD)).append(Component.space())
              : container.state == AppraisalState.CLEANED ? Component.empty()
                  .append(Component.text("Cleaned").color(NamedTextColor.GOLD))
                  .append(Component.space()) : Component.empty();
      return label
          .append(Component.text("Unidentified Gem ("))
          .append(rarityLabel)
          .append(Component.text(")")).color(
              NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      builder.addLine(Component.empty());
      builder.addLine(Component.text("An unusually bright gem,")
          .color(NamedTextColor.DARK_GRAY));
      builder.addLine(Component.text("its luster untouched by soil or time.")
          .color(NamedTextColor.DARK_GRAY));
      builder.addLine(Component.empty().append(Component.text("Perhaps a "))
          .append(Component.text("blacksmith").color(NamedTextColor.GOLD))
          .append(Component.text(" would know"))
          .color(NamedTextColor.DARK_GRAY));
      builder.addLine(Component.text("what lies beneath its shine.")
          .color(NamedTextColor.DARK_GRAY));

      builder.addLine(Component.empty());
      return builder.build();
    }

    @Override
    public @NotNull IRarity getRarity() {
      return container.rarity;
    }

    @Override
    public AppraisalState getState() {
      return container.state;
    }
  }

  static final class UnidentifiedGemContainer extends
      AbstractGemContainer<IUnidentifiedGemContainerView> implements
      IUnidentifiedGemContainer {

    @Expose
    @NotNull
    @SerializedName("rarity")
    private IRarity rarity;

    @Expose
    @SerializedName("state")
    private AppraisalState state;

    UnidentifiedGemContainer(NamespacedKey containerKey, ISocketColor color,
        @NotNull IRarity rarity) {
      super(containerKey, color);
      this.rarity = rarity;
      this.state = AppraisalState.INITIAL;
    }

    @Override
    public void setRarity(@NotNull IRarity rarity) {
      this.rarity = rarity;
    }

    @Override
    public void setState(AppraisalState state) {
      this.state = state;
    }

    @Override
    protected IUnidentifiedGemContainerView buildView() {
      return new UnidentifiedGemContainerView(this);
    }
  }

  static final class UnidentifiedGemFactory extends
      ContainerHolderFactory<IUnidentifiedGem, IUnidentifiedGemContainer, IUnidentifiedGemContainerView> implements
      IUnidentifiedGemFactory {

    private final IRegistry<IRarity> rarityRegistry;
    private final IRandomSelector<IRarity> raritySelector;
    private final IRegistry<ISocketColor> colorRegistry;
    private final IRandomSelector<ISocketColor> colorSelector;
    private final IRegistry<IGemEffect> effectRegistry;

    @Inject
    UnidentifiedGemFactory(IRegistry<IRarity> rarityRegistry,
        @Named("rarity-selector") IRandomSelector<IRarity> raritySelector,
        IRegistry<ISocketColor> colorRegistry,
        @Named("color-selector") IRandomSelector<ISocketColor> colorSelector,
        IRegistry<IGemEffect> effectRegistry) {
      this.rarityRegistry = rarityRegistry;
      this.raritySelector = raritySelector;
      this.colorRegistry = colorRegistry;
      this.colorSelector = colorSelector;
      this.effectRegistry = effectRegistry;
    }

    @Override
    protected NamespacedKey getContainerKey() {
      return UNIDENTIFIED_GEM_CONTAINER_KEY;
    }

    @Override
    public IUnidentifiedGem create(@NotNull ItemStack stack, @NotNull ISocketColor color,
        @NotNull IRarity rarity)
        throws IllegalArgumentException, NullPointerException {
      Preconditions.checkNotNull(color);
      Preconditions.checkNotNull(rarity);
      Preconditions.checkArgument(rarityRegistry.isRegistered(rarity));
      Preconditions.checkArgument(colorRegistry.isRegistered(color));
      UnidentifiedGemContainer container = new UnidentifiedGemContainer(
          this.getContainerKey(), color, rarity);
      return new UnidentifiedGem(stack, container);
    }

    @Override
    protected IUnidentifiedGem create(ItemStack stack, IUnidentifiedGemContainer container) {
      return new UnidentifiedGem(stack, container);
    }

    @Override
    public IUnidentifiedGem create(@NotNull ItemStack stack, @NotNull IRarity rarity)
        throws IllegalArgumentException, NullPointerException {
      ISocketColor color = colorSelector.getRandom(Taric.getRandom());
      return create(stack, color, rarity);
    }

    @Override
    protected Class<? extends IUnidentifiedGemContainer> getContainerImplClazz() {
      return UnidentifiedGemContainer.class;
    }

    @Override
    public IUnidentifiedGem create(@NotNull ItemStack stack)
        throws IllegalArgumentException, NullPointerException {
      IRarity rarity = raritySelector.getRandom(Taric.getRandom());
      return create(stack, rarity);
    }
  }

  @Override
  public @NotNull ItemStack getStack() {
    return stack;
  }
}
