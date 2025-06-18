package org.aincraft.container;

import com.google.common.collect.ForwardingSet;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public final class TypeSet extends ForwardingSet<Material> {

  @NotNull
  private final Set<Material> typeSet;

  TypeSet(@NotNull Set<Material> typeSet) {
    this.typeSet = typeSet;
  }

  @Override
  protected @NotNull Set<Material> delegate() {
    return typeSet;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static TypeSet single(Material material) {
    Set<Material> typeSet = new HashSet<>();
    typeSet.add(material);
    return new TypeSet(typeSet);
  }

  public static TypeSet withSuffix(String suffix) {
    Set<Material> typeSet = new HashSet<>();
    for (Material material : Material.values()) {
      String name = material.name();
      if (name.endsWith(suffix)) {
        typeSet.add(material);
      }
    }
    return new TypeSet(typeSet);
  }

  public Builder toBuilder() {
    return new Builder(typeSet);
  }

  public static class Builder {

    private final Set<Material> typeSet;

    public Builder() {
      this(new HashSet<>());
    }

    public Builder(Set<Material> typeSet) {
      this.typeSet = typeSet;
    }

    public Builder union(Set<Material> ... typeSets) {
        for (Set<Material> typeSet : typeSets) {
          this.typeSet.addAll(typeSet);
        }
        return this;
    }

    public TypeSet build() {
      return new TypeSet(typeSet);
    }
  }
}
