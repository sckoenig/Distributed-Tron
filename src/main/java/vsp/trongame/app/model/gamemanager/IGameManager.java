package vsp.trongame.app.model.gamemanager;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.datatypes.GameState;

import java.util.Map;

/**
 * Manages the player and the state of the game.
 */
public interface IGameManager {

    /**
     * Sets the color of the player who won as the winnerColor.
     * @param winnerColor the color of the player who won
     */
    void setWinnerColor(String winnerColor);

    /**
     * Sets the game result if someone won or if there was a draw.
     * @param result of the game either a draw or if there is one winner
     */
    void setGameResult(String result);

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
