package org.aincraft.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import org.aincraft.Taric;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.api.config.IConfiguration;
import org.bukkit.Material;
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
            .registerTypeAdapter(Key.class, new KeyAdapter())
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    );    bind(Plugin.class).toInstance(plugin);
    bind(Taric.class).asEagerSingleton();
    ConfigurationFactory configurationFactory = new ConfigurationFactory(plugin);
    for (Entry<String, String> entry : configs.entrySet()) {
      bind(IConfiguration.class).annotatedWith(Names.named(entry.getKey()))
          .toInstance(configurationFactory.yaml(entry.getValue()));
    }
  }

  public static class MaterialAdapter extends TypeAdapter<Material> {

    @Override
    public void write(JsonWriter jsonWriter, Material material) throws IOException {
      jsonWriter.value(material.toString());
    }

    @Override
    public Material read(JsonReader jsonReader) throws IOException {
      return Material.valueOf(jsonReader.nextString());
    }
  }

  public static class KeyAdapter extends TypeAdapter<Key> {
    @Override
    public void write(JsonWriter out, Key key) throws IOException {
      out.value(key.namespace() + ":" + key.key());
    }

    @Override
    public Key read(JsonReader in) throws IOException {
      String raw = in.nextString();
      String[] parts = raw.split(":", 2);
      if (parts.length != 2) throw new JsonParseException("Invalid Key format: " + raw);
      return new NamespacedKey(parts[0], parts[1]);
    }
  }

  public static class NamespacedKeyAdapter extends TypeAdapter<NamespacedKey> {

    @Override
    public void write(JsonWriter out, NamespacedKey key) throws IOException {
      out.value(key.getNamespace() + ":" + key.getKey());
    }

    @Override
    public NamespacedKey read(JsonReader in) throws IOException {
      String raw = in.nextString();
      String[] parts = raw.split(":", 2);
      if (parts.length != 2) throw new JsonParseException("Invalid NamespacedKey: " + raw);
      return new NamespacedKey(parts[0], parts[1]);
    }
  }
}