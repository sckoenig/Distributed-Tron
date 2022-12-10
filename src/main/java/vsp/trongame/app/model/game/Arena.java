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
        arenaField[coordinate.y][coordinate.x] = playerId;
    }

    @Override
    public void deletePlayerPositions(List<IPlayer> crashedPlayer) {
        for (int columns = 0; columns < arenaField.length; columns++) {
            for (int row = 0; row < arenaField[columns].length; row++) {
                if (arenaField[columns][row] !=0) {
                    for (IPlayer player: crashedPlayer) {
                        if(arenaField[columns][row] == player.getId()){
                            arenaField[columns][row] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean detectCollision(Coordinate coordinate) {
        return (arenaField.length == coordinate.x + 1 || arenaField[0].length == coordinate.y + 1) || arenaField[coordinate.y][coordinate.x] != 0;
    }
}
