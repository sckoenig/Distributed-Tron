package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.datatypes.Direction;
import vsp.trongame.app.model.datatypes.DirectionChange;

import java.util.List;

/**
 * Implements the interface IPlayer.
 */
public class Player implements IPlayer{

    private int id;
    private boolean alive;
    private List<Coordinate> coordinates;
    private Color color;
    private Direction direction;
    private DirectionChange nextAction;

    /**
     * Constructor player, where a player with a color and an id ist created.
     * @param color for the player
     * @param id for the player
     */
    public Player(Color color, int id){
        this.color = color;
        this.id = id;
    }

    @Override
    public Coordinate getHeadPosition() {
        return null;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public void addCoordinate(Coordinate coordinate) {

    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void crash() {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setNextDirectionChange(DirectionChange directionChange) {

    }

    @Override
    public Direction performDirectionChange() {
        return null;
    }
}
