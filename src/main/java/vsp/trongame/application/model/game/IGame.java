package vsp.trongame.application.model.game;

import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.Modus;
import vsp.trongame.application.model.datatypes.Steer;
import vsp.trongame.application.model.gamemanagement.IGameManager;

import java.util.concurrent.ExecutorService;

/**
 * Provides funcionality for influencing a Game's state for other components such as the GameManagement component.
 */
public interface IGame {

    /**
     * Initializes this Game with necessary dependencies.
     * @param modus modus the game should use
     * @param speed speed the game should use
     * @param rows number of rows arena should have
     * @param columns number of columns arena should have
     * @param waitingTimer the time the game waits for registration
     * @param endingTimer the time the game remains in its finishing state
     * @param executorService executor service for threads to use
     */
    void initialize(Modus modus, int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService);

    /**
     * Prepares the game to start, sets a timer for the countdown and creates an arena.
     * @param playerCount the accepted playerCount
     */
    void prepareForRegistration(int playerCount);

    /**
     * The game saves which observers are informing the game and creates the sum of players.
     * @param gameManager a manager observing the state of the game
     * @param listener a listener that is listening for game updates
     * @param listenerId the listener's registration id
     * @param managedPlayerCount the sum of players that the manager is going to manage
     */
    void register(IGameManager gameManager, IUpdateListener listener, int listenerId, int managedPlayerCount);

    /**
     * Based on the player id and the direction in the steer objects the players' directions are changed.
     * @param steer a player's steer event
     */
    void handleSteer(Steer steer);
}
