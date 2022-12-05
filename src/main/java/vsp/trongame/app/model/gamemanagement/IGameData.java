package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.List;
import java.util.Map;

/**
 * Manages the Model's Data.
 */
public interface IGameData {

    /**
     * Updates the game's results.
     * @param winner Color of the winner or default Color on draw
     * @param result the Game's result
     */
    void updateGameResult(TronColor winner, GameResult result);

    /**
     * Updates the game's keymappings.
     * @param mappings the key mappings
     */
    void updateKeyMappings(Map<String, TronColor> mappings);

    /**
     * Updates the game's arena size.
     * @param rows the arena's number of rows
     * @param columns the arena's number of columns
     */
    void updateArenaSize(int rows, int columns);

    /**
     * Updates the game's player data.
     * @param players Map of player colors and coordinates
     */
    void updatePlayers(Map<TronColor, List<Coordinate>> players);

    /**
     * Updates the model's state.
     * @param state new state
     */
    void updateState(String state);

    /**
     * Updates the counter for a countdown by decreasing it.
     * @param decrease value by which counter is decreased.
     */
    void updateCountDownCounter(int decrease);



}
