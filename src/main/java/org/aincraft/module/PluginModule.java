package org.aincraft.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.aincraft.Taric;
import org.aincraft.api.config.IConfiguration;
import org.aincraft.api.container.ISocketColor;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.database.Extractor;
import org.aincraft.database.Extractor.ResourceExtractor;
import org.aincraft.database.IDatabase;
import org.aincraft.effects.IGemEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public final class PluginModule extends AbstractModule {

  private final Plugin plugin;
  private final Map<String, String> configs;

  public PluginModule(Plugin plugin, Map<String, String> configs) {
    this.plugin = plugin;
    this.configs = configs;
  }

  @Override
  protected void configure() {
    bind(Gson.class).toInstance(
        new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Key.class, new KeyAdapter())
            .registerTypeAdapter(IGemEffect.class, new EffectAdapter())
            .registerTypeAdapter(ISocketColor.class, new ColorAdapter())
            .registerTypeAdapter(Component.class, new ComponentAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    );
    bind(Plugin.class).toInstance(plugin);
    bind(Taric.class).asEagerSingleton();
    ConfigurationFactory configurationFactory = new ConfigurationFactory(plugin);
    for (Entry<String, String> entry : configs.entrySet()) {
      bind(IConfiguration.class).annotatedWith(Names.named(entry.getKey()))
          .toInstance(configurationFactory.yaml(entry.getValue()));
    }
    bind(IDatabase.class).toProvider(StorageProvider.class).in(Singleton.class);
    bind(Extractor.class).to(ResourceExtractor.class);
  }

  public static class KeyAdapter extends TypeAdapter<Key> {

    @Override
    public void write(JsonWriter out, Key key) throws IOException {
      if (key == null) {
        out.nullValue(); // write null properly
        return;
      }
      out.value(key.toString());
    }

    @Override
    public Key read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull(); // consume null
        return null;
      }

      String raw = in.nextString();
      String[] parts = raw.split(":", 2);
      if (parts.length != 2) {
        throw new JsonParseException("Invalid Key format: " + raw);
      }
      return new NamespacedKey(parts[0], parts[1]);
    }
  }

  public static class ComponentAdapter extends TypeAdapter<Component> {

    @Override
    public void write(JsonWriter out, Component component) throws IOException {
      if (component == null) {
        out.nullValue();
        return;
      }
      String serialized = MiniMessage.miniMessage().serialize(component);
      out.value(serialized);
    }

    @Override
    public Component read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String raw = in.nextString();
      return MiniMessage.miniMessage().deserialize(raw);
    }
  }

  public static class ColorAdapter extends TypeAdapter<ISocketColor> {

    @Override
    public void write(JsonWriter out, ISocketColor color) throws IOException {
      if (color == null) {
        out.nullValue();
        return;
      }
      out.value(color.key().toString());
    }

    @Override
    public ISocketColor read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      String raw = in.nextString();
      String[] parts = raw.split(":", 2);
      if (parts.length != 2) {
        throw new JsonParseException("Invalid Key format: " + raw);
      }
      NamespacedKey key = new NamespacedKey(parts[0], parts[1]);
      return Taric.getColors().get(key);
    }
  }

  public static class EffectAdapter extends TypeAdapter<IGemEffect> {

    @Override
    public void write(JsonWriter out, IGemEffect effect) throws IOException {
      if (effect == null) {
        out.nullValue();
        return;
      }
      out.value(effect.key().toString());
    }

    @Override
    public IGemEffect read(JsonReader in) throws IOException {
      String raw = in.nextString();
      String[] parts = raw.split(":", 2);
      if (parts.length != 2) {
        throw new JsonParseException("Invalid NamespacedKey: " + raw);
      }
      NamespacedKey key = new NamespacedKey(parts[0], parts[1]);
      return Taric.getEffects().get(key);
    }
  }
}