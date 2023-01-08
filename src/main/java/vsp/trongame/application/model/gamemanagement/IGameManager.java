package vsp.trongame.application.model.gamemanagement;

import vsp.trongame.application.model.datatypes.GameState;

import java.util.List;

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
    void handleManagedPlayers(int id, List<Integer> managedPlayers);

}
