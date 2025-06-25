package org.aincraft.container;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.aincraft.Taric;
import org.aincraft.api.container.IRarity;
import org.aincraft.api.container.gem.AppraisalState;
import org.aincraft.api.container.gem.IPreciousGem;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainer;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.registry.IRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class PreciousGem extends
    AbstractHolder<IPreciousGemContainer, IPreciousGemContainerView> implements IPreciousGem {

  static final NamespacedKey PRECIOUS_GEM_KEY = new NamespacedKey("taric", "precious");

  public PreciousGem(ItemStack stack, IPreciousGemContainer container) {
    super(stack, container);
  }

  @Override
  public @NotNull ItemStack getStack() {
    return stack;
  }

  private static final class View extends
      AbstractView<Container, IPreciousGemContainer, IPreciousGemContainerView> implements
      IPreciousGemContainerView {

    View(Container container) {
      super(container);
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

  static final class Container extends
      AbstractContainer<IPreciousGemContainerView> implements
      IPreciousGemContainer {

    @Expose
    @SerializedName("rarity")
    private IRarity rarity;

    @Expose
    @SerializedName("state")
    private AppraisalState state;

    Container(NamespacedKey containerKey, IRarity rarity) {
      super(containerKey);
      this.rarity = rarity;
      this.state = AppraisalState.INITIAL;
    }

    @Override
    public void setRarity(@NotNull IRarity rarity) {
      this.rarity = rarity;
    }

    @Override
    protected IPreciousGemContainerView buildView() {
      return new View(this);
    }

    @Override
    public void setState(AppraisalState state) {
      this.state = state;
    }
  }

  static final class Factory extends
      ItemHolderFactory<IPreciousGem, IPreciousGemContainer, IPreciousGemContainerView> implements
      IPreciousGemFactory {

    private final IRegistry<IRarity> rarityRegistry;
    private final IRandomSelector<IRarity> raritySelector;

    @Inject
    Factory(IRegistry<IRarity> rarityRegistry,
        IRandomSelector<IRarity> raritySelector) {
      this.rarityRegistry = rarityRegistry;
      this.raritySelector = raritySelector;
    }

    @Override
    protected Class<? extends IPreciousGemContainer> getContainerImplClazz() {
      return Container.class;
    }

    @Override
    protected NamespacedKey getContainerKey() {
      return PRECIOUS_GEM_KEY;
    }

    @Override
    protected IPreciousGem create(ItemStack stack, IPreciousGemContainer container) {
      return new PreciousGem(stack, container);
    }

    @Override
    public IPreciousGem create(ItemStack stack, IRarity rarity) throws IllegalArgumentException {
      Preconditions.checkArgument(rarityRegistry.isRegistered(rarity));
      Container container = new Container(this.getContainerKey(), rarity);
      return new PreciousGem(stack, container);
    }

    @Override
    public IPreciousGem create(ItemStack stack) throws IllegalArgumentException {
      IRarity rarity = raritySelector.getRandom(Taric.getRandom());
      return create(stack, rarity);
    }
  }
}
