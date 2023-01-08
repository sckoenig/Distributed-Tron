package vsp.trongame.application.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.application.model.datatypes.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the interface Arena.
 */
public class Arena implements IArena{

    private final int[][] arenaField;
    private final int rows;
    private final int columns;

    public Arena(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.arenaField = new int[rows][columns];
    }

    @Override
    public void addPlayerPosition(int playerId, Coordinate coordinate) {
        arenaField[coordinate.y][coordinate.x] = playerId+1;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    @Override
    public void deletePlayerPositions(List<Integer> crashedPlayer) {
        for (int column = 0; column < arenaField.length; column++) {
            for (int row = 0; row < arenaField[column].length; row++) {
                if (arenaField[column][row] !=0) {
                    for (Integer playerId: crashedPlayer) {
                        if(arenaField[column][row] == playerId+1){
                            arenaField[column][row] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean detectCollision(Coordinate coordinate) {
        return (coordinate.x < 0 || coordinate.y < 0 ||
                arenaField.length <= coordinate.y ||
                arenaField[0].length <= coordinate.x) ||
                arenaField[coordinate.y][coordinate.x] != 0;
    }

    @Override
    public List<Coordinate> calculateFairStartingCoordinates(int playerCount) {
        List<Coordinate> fairPositions = new ArrayList<>();
        switch (playerCount) {
            case 1,2 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(columns-1, rows-1));
            }
            case 3 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(columns-1, rows-1));
                fairPositions.add(new Coordinate(columns-1, 0));
            }
            case 4 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(columns-1, rows-1));
                fairPositions.add(new Coordinate(columns-1, 0));
                fairPositions.add(new Coordinate(0, rows-1));
            }
            case 5 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(columns-1, rows-1));
                fairPositions.add(new Coordinate(columns-1, 0));
                fairPositions.add(new Coordinate(0, rows-1));
                fairPositions.add(new Coordinate(columns / 2, rows / 2));
            }
            case 6 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(columns-1, rows-1));
                fairPositions.add(new Coordinate(columns-1, 0));
                fairPositions.add(new Coordinate(0, rows-1));
                fairPositions.add(new Coordinate( 0, rows / 2));
                fairPositions.add(new Coordinate(columns-1, rows / 2));
            }
            default -> {
            }
        }
        return fairPositions;
    }

    @Override
    public Direction calculateStartingDirection(Coordinate coordinate) {
        if (coordinate.x == 0) return Direction.RIGHT;
        else if (coordinate.x == columns-1) return Direction.LEFT;
        else return Direction.DOWN;
    }

}
