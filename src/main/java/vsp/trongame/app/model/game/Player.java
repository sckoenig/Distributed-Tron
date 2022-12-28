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

    private final int id;
    private final List<Coordinate> coordinates;
    private final TronColor color;
    private Direction direction;
    private boolean alive;

    public Player(TronColor color, int id) {
        this.color = color;
        this.id = id;
        this.alive = true;
        this.coordinates = new ArrayList<>();
    }

    @Override
    public Coordinate getHeadPosition() {
        return this.coordinates.get(this.coordinates.size()-1);
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    @Override
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
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
        this.coordinates.remove(coordinates.size()-1);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void performDirectionChange(DirectionChange directionChange) {
        this.direction = Direction.getNextDirection(direction, directionChange);
    }

}
