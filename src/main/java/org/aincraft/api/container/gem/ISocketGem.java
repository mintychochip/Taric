package org.aincraft.api.container.gem;

import org.aincraft.api.container.ISocketColor;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemContainer;
import org.aincraft.api.container.gem.ISocketGem.ISocketGemContainerView;
import org.aincraft.effects.IGemEffect;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISocketGem extends
    IItemContainerHolder<ISocketGemContainer, ISocketGemContainerView> {

  interface ISocketGemContainer extends IEffectContainer<ISocketGemContainerView> {

    @NotNull
    ISocketColor getSocketColor();

    @Nullable
    IGemEffect getEffect();

    int getRank();

    void move(
        @NotNull IItemContainerHolder<? extends IEffectContainer<?>, ? extends IEffectContainerView> holder)
        throws IllegalArgumentException, NullPointerException, IllegalStateException;

    /**
     * Determines whether this gem can be merged with another specified gem.
     * <p>
     * The merge check should not modify either gem's state.
     *
     * @param other the other gem to check against
     * @return true if this gem can be mergeable, false otherwise
     */
    boolean canMerge(@Nullable ISocketGem other);


    /**
     * Merges the specified gem into this one, increasing the rank by one.
     * <p>
     * The operation should be associative and commutative, e.g. merging gem A with B should yield
     * the same result as merging B with A, assuming both targets are valid.
     * <p>
     * It is advised to check first whether gems are mergeable by calling
     * {@link #canMerge(ISocketGem)}.
     *
     * @param other the gem being merged
     * @throws IllegalArgumentException some merging criteria failed, e.g. a gem must have the same
     *                                  effect as the other
     * @throws IllegalStateException    the current effect is null or rank is less than or equal to
     *                                  0
     * @throws NullPointerException     the other gem or it's effect is null
     */
    void merge(ISocketGem other)
        throws IllegalArgumentException, IllegalStateException, NullPointerException;
  }

  interface ISocketGemContainerView extends IEffectContainerView {

    @NotNull
    ISocketColor getSocketColor();

    @Nullable
    IGemEffect getEffect();

    int getRank();
  }

  interface ISocketGemFactory extends
      IItemHolderFactory<ISocketGem, ISocketGemContainer, ISocketGemContainerView> {

    @NotNull
    ISocketGem create(Material material, ISocketColor socketColor);

    @NotNull
    ISocketGem create(Material material, IGemEffect effect, int rank, boolean force);

    @NotNull
    ISocketGem create(Material material, IGemEffect effect);
  }

  @NotNull
  ISocketColor getSocketColor();
}
