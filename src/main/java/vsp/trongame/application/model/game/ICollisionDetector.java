package vsp.trongame.application.model.game;

import java.util.List;

/**
 * Detects Collision of the Player with a wall or with at least on other Player.
 */
public interface ICollisionDetector {

    /**
     * Detects if a player is crashed because of a collision with the wall or at least one other Player.
     * If a player collides with a wall or the shadow of another player the player is crashed.
     * If two or more players collide, they are all crashed.
     * The positions of any crashed player are removed from the arena.
     * @param alivePlayer list with all players that are still alive
     * @param arena the arena
     */
    void detectCollision(List<IPlayer> alivePlayer, IArena arena);
}
