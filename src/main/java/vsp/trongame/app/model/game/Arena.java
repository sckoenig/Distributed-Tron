package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;

import java.util.List;

/**
 * Implements the interface Arena.
 */
public class Arena implements IArena{

    private int[][] arenaField;

    /**
     * Constructor arena where a new Arena is initialized.
     * @param rows the number of rows
     * @param columns the number of columns
     */
    public Arena(int rows, int columns){
        this.arenaField = new int[columns][rows];
    }

    @Override
    public void addPlayerPosition(int playerId, Coordinate coordinate) {

    }

    @Override
    public void deletePlayerPositions(List<Integer> playerIds) {

    }

    @Override
    public boolean detectCollision(Coordinate coordinate) {
        return false;
    }
}
