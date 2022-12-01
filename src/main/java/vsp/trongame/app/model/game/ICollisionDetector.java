package vsp.trongame.app.model.game;

import java.util.List;

/**
 * Detects Collision of the Player with a wall or with at least on other Player.
 */
public interface ICollisionDetector {

    /**
     * detects if a player is crashed because of a collision with the wall or at least one other Player.
     * If a player collides with a wall or other player we crash the bike and delete all positions from our
     * arena.
     * @param alivePlayer list with all players that are still alive
     * @param arena the arena
     */
    void detectCollision(List<IPlayer> alivePlayer, IArena arena);
}
