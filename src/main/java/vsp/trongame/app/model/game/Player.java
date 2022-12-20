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
    public Direction performDirectionChange(DirectionChange change) {
       /*switch (this.direction){
           case LEFT -> {
               if(this.nextAction == DirectionChange.LEFT_STEER){
                   this.direction = Direction.DOWN;
                   this.nextAction = DirectionChange.NONE;
               } else if (this.nextAction == DirectionChange.RIGHT_STEER) {
                   this.direction = Direction.UP;
                   this.nextAction = DirectionChange.NONE;
               }
           }
           case RIGHT -> {
               if(this.nextAction == DirectionChange.LEFT_STEER){
                   this.direction = Direction.UP;
                   this.nextAction = DirectionChange.NONE;
               } else if (this.nextAction == DirectionChange.RIGHT_STEER) {
                   this.direction = Direction.DOWN;
                   this.nextAction = DirectionChange.NONE;
               }
           }
           case DOWN -> {
               if(this.nextAction == DirectionChange.LEFT_STEER){
                   this.direction = Direction.RIGHT;
                   this.nextAction = DirectionChange.NONE;
               } else if (this.nextAction == DirectionChange.RIGHT_STEER) {
                   this.direction = Direction.LEFT;
                   this.nextAction = DirectionChange.NONE;
               }
           }
           case UP -> {
               if(this.nextAction == DirectionChange.LEFT_STEER){
                   this.direction = Direction.LEFT;
                   this.nextAction = DirectionChange.NONE;
               } else if (this.nextAction == DirectionChange.RIGHT_STEER) {
                   this.direction = Direction.RIGHT;
                   this.nextAction = DirectionChange.NONE;
               }
           }
       }

        */
       return this.direction;
    }

}
