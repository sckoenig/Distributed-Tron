package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.datatypes.Direction;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the interface IPlayer.
 */
public class Player implements IPlayer {

    private int id;
    private boolean alive;
    private List<Coordinate> coordinates;
    private TronColor color;
    private Direction direction;
    private DirectionChange nextAction;

    /**
     * Constructor player, where a player with a color and an id ist created.
     *
     * @param color for the player
     * @param id    for the player
     */
    public Player(TronColor color, int id) {
        this.color = color;
        this.id = id;
        this.alive = true;
        this.coordinates = new ArrayList<>();
    }

    @Override
    public Coordinate getHeadPosition() {
        //TODO
        return null;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public void addCoordinate(Coordinate coordinate) {
        //TODO
    }

    @Override
    public TronColor getColor() {
        return color;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void crash() {
        this.alive = false;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setNextDirectionChange(DirectionChange directionChange) {
        //TODO
    }

    @Override
    public void setDirection(Direction direction) {
        //TODO
    }

    @Override
    public Direction performDirectionChange() {
        return null;
    }

}
