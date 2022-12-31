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
    private DirectionChange nextDirectionChange; //one per game tick

    public Player(TronColor color, int id) {
        this.color = color;
        this.id = id;
        this.alive = true;
        this.coordinates = new ArrayList<>();
        this.nextDirectionChange = DirectionChange.NO_STEER;
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
    public void setNextDirectionChange(DirectionChange directionChange) {
        this.nextDirectionChange = directionChange;
    }

    @Override
    public Direction performDirectionChange() {
        this.direction = Direction.getNextDirection(direction, nextDirectionChange);
        this.nextDirectionChange = DirectionChange.NO_STEER;
        return this.direction;
    }


}
