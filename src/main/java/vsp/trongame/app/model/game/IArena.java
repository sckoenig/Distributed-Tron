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
     * Deletes all occurrences of all the playerIds in the List, in the arena.
     * @param crashedPlayerIds all the playerIds of the players which crashed.
     */
    void deletePlayerPositions(List<Integer> crashedPlayerIds);

    /**
     * Detects if a player crashes with an arena wall or the shadow of another player.
     * @param coordinate head coordinate of the player
     * @return true if the player crashed, false otherwise
     */
    boolean detectCollision(Coordinate coordinate);



}
