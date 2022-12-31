package vsp.trongame.app.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.gamemanagement.IGameManager;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Provides funcionality for influencing a Game's state for other components such as the GameManagement component.
 */
public interface IGame {

    void initialize(int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService);

    /**
     * Prepares the game to start, sets a timer for the countdown and creates an arena.
     * @param playerCount the accepted playerCount
     */
    void prepare(int playerCount);

    /**
     * The game saves which observers are informing the game and creates the sum of players.
     * @param gameManager a manager observing the state of the game
     * @param listener a listener that is listening for game updates
     * @param listenerId the listener's registration id
     * @param managedPlayerCount the sum of players that the manager is going to manage
     */
    void register(IGameManager gameManager, ITronModel.IUpdateListener listener, int listenerId, int managedPlayerCount);

    /**
     * Based on the player id and the direction in the steer objects the players' directions are changed.
     * @param steers a list of the player's steer events
     */
    void handleSteer(Steer steer);
}
