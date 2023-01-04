package vsp.trongame.application.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.application.model.datatypes.Direction;
import vsp.trongame.application.model.datatypes.DirectionChange;

import java.util.List;

/**
 * The player which manages the players coordinates and if the player is alive.
 */
public interface IPlayer {

    /**
     * Gets the coordinates of the head of the player.
     * @return the coordinates of the head
     */
    Coordinate getHeadPosition();

    /**
     * Gets all coordinates of an player.
     * @return the list with all coordinates
     */
    List<Coordinate> getCoordinates();

    /**
     * Adds an coordinate to the list of all coordinates.
     * @param coordinate the to be added coordinate
     */
    void addCoordinate(Coordinate coordinate);

    /**
     * checks if the player is still alive.
     * @return if the player is alive
     */
    boolean isAlive();

    /**
     * When the player crashes the player is set as not alive.
     */
    void crash();

    /**
     * Gets the id of a player.
     * @return the id
     */
    int getId();

    /**
     * Sets the direction of the player.
     * @param direction the player's new direction.
     * */
    void setDirection(Direction direction);


    /**
     * Sets
     * @param directionChange the direction of change in a steer event.
     */
    void setNextDirectionChange(DirectionChange directionChange);

    /**
     * Sets the current direction based on the current nextDirectionChange.
     */
    Direction performDirectionChange();
}
