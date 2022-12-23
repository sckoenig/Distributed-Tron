package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.util.datatypes.GameState;
import vsp.trongame.app.model.util.datatypes.TronColor;

import java.util.Map;

/**
 * Manages the state of the game.
 */
public interface IGameManager {

    /**
     * Informs the GameManager about the Game's {@link GameState}.
     * @param gameState the next modelState
     */
    void handleGameState(GameState gameState);

    /**
     * Informs the GameManager about the players it should manage.
     * @param id registration id
     * @param managedPlayers the ID and color of the players managed by the GameManager
     */
    void handleManagedPlayers(int id, Map<Integer, TronColor> managedPlayers);

    /**
     * Informs the GameManager about a game tick.
     * @param tickCount number of current tick.
     */
    void handleGameTick(int tickCount);
}
