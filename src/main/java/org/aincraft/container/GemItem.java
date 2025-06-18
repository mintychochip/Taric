package org.aincraft.container;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import org.aincraft.Taric;
import org.aincraft.container.IQueueLoader.IQueueLoaderHolder;
import org.aincraft.effects.EffectQueuePool.EffectInstance;
import org.aincraft.effects.IGemEffect;
import org.aincraft.effects.triggers.TriggerType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GemItem implements IQueueLoaderHolder<EffectInstance> {

  private static final NamespacedKey GEM_ITEM_KEY = new NamespacedKey(Taric.getPlugin(), "item");
  private final @NotNull GemItemContents contents;
  private final @NotNull ItemStack item;
  private final Map<TriggerType,IQueueLoader<EffectInstance>> loaders = new HashMap<>();

  public GemItem(@NotNull GemItemContents contents, @NotNull ItemStack item) {
    this.item = item;
    this.contents = contents;
  }

  @Override
  public String toString() {
    return Taric.getGson().toJson(this);
  }

  public static boolean is(@NotNull ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return false;
    }
    return stack.getItemMeta().getPersistentDataContainer().has(GEM_ITEM_KEY);
  }

  public void setContents(Consumer<GemItemContents> contentConsumer) {
    contentConsumer.accept(contents);
    setContents(item, contents);
  }

  public static void setContents(@NotNull ItemStack stack, GemItemContents contents) {
    Material material = stack.getType();
    if (material.isAir()) {
      return;
    }
    ItemMeta meta = stack.getItemMeta();
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    String jsonContents = Taric.getGson().toJson(contents);
    pdc.set(GEM_ITEM_KEY, PersistentDataType.STRING, jsonContents);
    stack.setItemMeta(meta);
  }

  public static GemItem from(@NotNull ItemStack stack) {
    Material material = stack.getType();
    if (material.isAir()) {
      return null;
    }
    ItemMeta meta = stack.getItemMeta();
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    if (!pdc.has(GEM_ITEM_KEY)) {
      return null;
    }
    String jsonContents = pdc.get(GEM_ITEM_KEY, PersistentDataType.STRING);
    GemItemContents contents = Taric.getGson()
        .fromJson(jsonContents, GemItemContents.class);
    return new GemItem(contents, stack);
  }

  public static GemItem create(@NotNull ItemStack stack, int sockets) {
    GemItemContents contents = new GemItemContents(sockets);
    setContents(stack, contents);
    return new GemItem(contents, stack);
  }

  public static Map<@NotNull EquipmentSlot, @NotNull GemItem> getAllItems(@NotNull Player player) {
    Map<EquipmentSlot, GemItem> result = new HashMap<>();
    PlayerInventory inventory = player.getInventory();
    for (EquipmentSlot slot : EquipmentSlot.values()) {
      if (slot == EquipmentSlot.BODY) {
        continue;
      }
      @Nullable GemItem gemItem = from(inventory.getItem(slot));
      if (gemItem != null) {
        result.put(slot, gemItem);
      }
    }
    return result;
  }

  public @NotNull GemItemContents contents() {
    return contents;
  }

  public @NotNull ItemStack item() {
    return item;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (GemItem) obj;
    return Objects.equals(this.contents, that.contents) &&
        Objects.equals(this.item, that.item);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contents, item);
  }

  @Override
  public IQueueLoader<EffectInstance> getLoader(TriggerType triggerType) {
    return loaders.computeIfAbsent(triggerType, key -> new IQueueLoader<EffectInstance>() {
      @Override
      public void load(Queue<EffectInstance> queue) {

      }
    });
  }


  public static final class GemItemContents {

    @SerializedName("sockets")
    private int sockets;

    @SerializedName("sockets-used")
    private int socketsUsed;

    @SerializedName("effects")
    private Map<String, Integer> effects;

    public GemItemContents() {
      this(0);
    }

    public GemItemContents(int sockets) {
      this.sockets = sockets;
      this.socketsUsed = 0;
      this.effects = new HashMap<>();
    }

    public int getSockets() {
      return sockets;
    }

    public int getSocketsUsed() {
      return socketsUsed;
    }

    public void setSocketsUsed(int socketsUsed) {
      this.socketsUsed = socketsUsed;
    }

    public GemItemContents setEffect(IGemEffect effect, int level) {
      if (effect == null) {
        return this;
      }
      level = Math.min(effect.getMaxRank(), level);
      effects.put(effect.key().value(), level);
      return this;
    }

    @NotNull
    public Map<String, Integer> getEffects() {
      return effects;
    }

    @Override
    public String toString() {
      return Taric.getGson().toJson(this);
    }

    @Nullable
    public static GemItemContents getContents(ItemMeta meta) {
      PersistentDataContainer pdc = meta.getPersistentDataContainer();
      String contents = pdc.get(GEM_ITEM_KEY, PersistentDataType.STRING);
      if (contents == null) {
        return null;
      }
      return Taric.getGson().fromJson(contents, GemItemContents.class);
    }
  }


}
