package de.teamlapen.vampirism.api.entity.factions;

import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.ILordPlayer;
import de.teamlapen.vampirism.api.extensions.IPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Handles factions and levels for the player
 * Attached to all players as capability
 */
public interface IFactionPlayerHandler extends ILordPlayer, IPlayer {

    /**
     * Players can only join a faction if they are in no other.
     *
     * @return If the player can join the given faction
     */
    boolean canJoin(IPlayableFaction<?> faction);

    boolean canJoin(Holder<? extends IPlayableFaction<?>> faction);

    /**
     * Checks currents factions {@link IFactionPlayer#canLeaveFaction()}
     */
    boolean canLeaveFaction();

    /**
     * @return The currently active faction. Can be null
     */
    @Deprecated
    @Nullable
    <T extends IFactionPlayer<T>> IPlayableFaction<T> getCurrentFaction();

    @Nullable
    Holder<? extends IPlayableFaction<?>> getFaction();

    /**
     * @return The currently active faction player. Can be null
     */
    @NotNull
    <T extends IFactionPlayer<T>> Optional<T> getCurrentFactionPlayer();

    /**
     * If no faction is active this returns 0.
     * Prefer using {@link IFactionPlayer#getLevel()} unless you are checking your own faction, since other factions might handle things differently
     *
     * @return the level of the currently active faction
     */
    int getCurrentLevel();

    /**
     * Makes some things easier.
     * Prefer using {@link IFactionPlayer#getLevel()} unless you are checking your own faction, since other factions might handle things differently
     *
     * @return If the faction is active: The faction level, otherwise 0
     */
    int getCurrentLevel(IPlayableFaction<?> f);

    int getCurrentLevel(Holder<? extends IPlayableFaction<?>> f);

    /**
     * If not in faction returns 0f
     *
     * @return Level/MaxLevel. Between 0f and 1f.
     */
    float getCurrentLevelRelative();

    /**
     * @return The player represented by this handler
     * @deprecated use {@link de.teamlapen.vampirism.api.extensions.IPlayer#asEntity()}
     */
    @Deprecated
    @NotNull
    Player getPlayer();

    /**
     * @return If the given faction is equal to the current one
     */
    boolean isInFaction(IFaction<?> f);

    <T extends IFaction<?>> boolean isInFaction(Holder<T> f);

    /**
     * Join the given faction and set the faction level to 1.
     * Only successful if {@link IFactionPlayerHandler#canJoin(IPlayableFaction)}
     */
    void joinFaction(@NotNull IPlayableFaction<?> faction);


    void joinFaction(@NotNull Holder<? extends IPlayableFaction<?>> faction);

    /**
     * Should be called if the entity attacked.
     * If this returns false the attack should be canceled
     *
     * @return If false the attack should be canceled
     */
    boolean onEntityAttacked(DamageSource src, float amt);

    /**
     * Set the players faction and it's level. Only use this if you are sure that you want to override the previous faction.
     *
     * @return If successful
     */
    boolean setFactionAndLevel(@Nullable IPlayableFaction<?> faction, int level);

    boolean setFactionAndLevel(@Nullable Holder<? extends IPlayableFaction<?>> faction, int level);

    /**
     * Set the level for a faction. Only works if the player already is in the given faction.
     * Use {@link IFactionPlayerHandler#joinFaction(IPlayableFaction)} to join a faction first or {@link IFactionPlayerHandler#setFactionAndLevel(IPlayableFaction, int)} if you are sure what you do
     *
     * @return If successful
     */
    boolean setFactionLevel(@NotNull Holder<? extends IPlayableFaction<?>> faction, int level);


    boolean setFactionLevel(@NotNull IPlayableFaction<?> faction, int level);

    /**
     * Set the players lord level.
     * Checks if player is in faction and at faction max level and if level is lower than max lord level
     *
     * @return if successful
     */
    boolean setLordLevel(int level);

    /**
     * Leave the current faction (if in any) by setting current faction to null and level to 0.
     *
     * @param die Whether to attack the player with deadly damage
     */
    void leaveFaction(boolean die);

    /**
     * Checks which skill trees are unlocked.
     * It locks and unlocks the skill trees accordingly.
     * <p>
     * It is called when the player level or lord level changes as well as when the player respawns. But it can be called at any time.
     */
    void checkSkillTreeLocks();
}
