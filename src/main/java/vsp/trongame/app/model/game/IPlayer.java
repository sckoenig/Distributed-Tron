package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.datatypes.Direction;
import vsp.trongame.app.model.datatypes.DirectionChange;

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
     * Gets the color of the player.
     * @return the color
     */
    Color getColor();

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
     * Sets the direction which the player wants to go next, if the player presses a new key before the
     * next round has started the old directionChange will be overwritten.
     * @param directionChange the next direction.
     */
    void setNextDirectionChange(DirectionChange directionChange);

    /**
     * Sets the current direction based on the next nextDirectionChange, once every move and changes
     * the nextDirectionChange to NONE.
     * @return the new direction
     */
    Direction performDirectionChange();
}
