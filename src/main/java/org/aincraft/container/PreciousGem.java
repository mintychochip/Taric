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
import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.IPreciousGem;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainer;
import org.aincraft.api.container.gem.IPreciousGem.IPreciousGemContainerView;
import org.aincraft.api.container.util.IRandomSelector;
import org.aincraft.registry.IRegistry;
import org.aincraft.util.Utils;
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
      Component rarityLabel = Component.text(Utils.toTitleCase(container.rarity.getName()))
          .color(container.rarity.getTextColor());
      return Component.empty()
          .append(Component.text("Unidentified Gem ("))
          .append(rarityLabel)
          .append(Component.text(")")).color(
              NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    protected ItemLore toItemLore() {
      ItemLore.Builder builder = ItemLore.lore();
      builder.addLine(Component.empty());
      ISocketColor socketColor = container.color;
      if (socketColor != null) {
        Component colorLabel = Component.empty()
            .append(Component.text(Utils.toTitleCase(socketColor.getName())))
            .decoration(TextDecoration.ITALIC, false).color(socketColor.getTextColor());
        builder.addLine(colorLabel);
        builder.addLine(Component.empty());
      }
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
    public @NotNull ISocketColor getColor() {
      return container.color;
    }

    @Override
    public @NotNull IRarity getRarity() {
      return container.rarity;
    }
  }

  static final class Container extends
      AbstractContainer<IPreciousGemContainerView> implements
      IPreciousGemContainer {

    @Expose
    @SerializedName("rarity")
    private IRarity rarity;

    @Expose
    @SerializedName("color")
    private ISocketColor color;

    Container(NamespacedKey containerKey, IRarity rarity, ISocketColor color) {
      super(containerKey);
      this.rarity = rarity;
      this.color = color;
    }

    @Override
    public void setSocketColor(@NotNull ISocketColor color) {
      this.color = color;
    }

    @Override
    public void setRarity(@NotNull IRarity rarity) {
      this.rarity = rarity;
    }

    @Override
    protected IPreciousGemContainerView buildView() {
      return new View(this);
    }
  }

  static final class Factory extends
      ItemHolderFactory<IPreciousGem, IPreciousGemContainer, IPreciousGemContainerView> implements
      IPreciousGemFactory {

    private final IRegistry<IRarity> rarityRegistry;
    private final IRegistry<ISocketColor> colorRegistry;
    private final IRandomSelector<IRarity> raritySelector;
    private final IRandomSelector<ISocketColor> colorSelector;

    @Inject
    Factory(IRegistry<IRarity> rarityRegistry, IRegistry<ISocketColor> colorRegistry,
        IRandomSelector<IRarity> raritySelector, IRandomSelector<ISocketColor> colorSelector) {
      this.rarityRegistry = rarityRegistry;
      this.colorRegistry = colorRegistry;
      this.raritySelector = raritySelector;
      this.colorSelector = colorSelector;
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
    public IPreciousGem create(ItemStack stack, IRarity rarity, ISocketColor color)
        throws IllegalArgumentException {
      Preconditions.checkArgument(rarityRegistry.isRegistered(rarity));
      Preconditions.checkArgument(colorRegistry.isRegistered(color));
      Container container = new Container(this.getContainerKey(), rarity, color);
      return new PreciousGem(stack, container);
    }

    @Override
    public IPreciousGem create(ItemStack stack, ISocketColor color)
        throws IllegalArgumentException {
      //TODO: remove static call
      IRarity rarity = raritySelector.getRandom(Taric.getRandom());
      return create(stack, rarity, color);
    }

    @Override
    public IPreciousGem create(ItemStack stack, IRarity rarity) throws IllegalArgumentException {
      ISocketColor color = colorSelector.getRandom(Taric.getRandom());
      return create(stack, rarity, color);
    }

    @Override
    public IPreciousGem create(ItemStack stack) throws IllegalArgumentException {
      IRarity rarity = raritySelector.getRandom(Taric.getRandom());
      return create(stack, rarity);
    }
  }
}
