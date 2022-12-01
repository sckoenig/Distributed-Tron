package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;

import java.util.List;

/**
 * The arena which stores and manages the positions of each bike.
 */
public interface IArena {
    /**
     * Adds the new position of the player on the arena.
     * @param playerId the id of player to the coordinate
     * @param coordinate the coordinate which is added to the shadow of a bike.
     */
    void addPlayerPosition(int playerId, Coordinate coordinate);

    /**
     * Deletes all occurrences of all the player in the List, in the arena.
     * @param playerIds all the player ids to the players which crashed.
     */
    void deletePlayerPositions(List<Integer> playerIds);

    /**
     * Detects if a player crashes with an arena wall.
     * @param coordinate of the player
     * @return if the player crashed with the wall
     */
    boolean detectWallCollision(Coordinate coordinate);
}
