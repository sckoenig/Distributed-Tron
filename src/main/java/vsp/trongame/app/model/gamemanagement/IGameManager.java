package vsp.trongame.app.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.Map;

/**
 * Manages the state of the game.
 */
public interface IGameManager {

    /**
     * The modelState changes to the next modelState based on the message.
     * @param gameState the next modelState
     */
    void handleGameState(GameState gameState);

    /**
     * Sets the players which are managed in the GameManager.
     * @param managedPlayers the ID and Starting Coordinate of the players managed by the GameManager
     */
    void setManagedPlayers(Map<Integer, TronColor> managedPlayers);
}
