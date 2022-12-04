package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.datatypes.GameState;

import java.util.Map;

/**
 * Manages the player and the state of the game.
 */
public interface IGameManager {


    /**
     * The modelState changes to the next modelState based on the message.
     * @param gameState the next modelState
     */
    void handleGameState(GameState gameState);

    /**
     * Sets the players which are managed in the GameManager.
     * @param managedPlayers the players which need to be managed by the GameManager
     */
    void setManagedPlayers(Map<Integer, Coordinate> managedPlayers);
}
