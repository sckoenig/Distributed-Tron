package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.ITronView;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.gamemanager.IGameManager;

import java.util.List;

/**
 * Manages the game events passed on what the observers inform the IGame about.
 */
public interface IGame {

    /**
     * Prepares the game to start, sets a timer for the countdown and creates an arena.
     * @param waitingTimer the time of the timer
     * @param playerCount the accepted playerCount
     */
    void prepare(int waitingTimer, int playerCount);

    /**
     * The game saves which observers are informing the game and creates the sum of players.
     * @param viewObservable the observer of the view
     * @param stateObserver the observer of the state
     * @param managedPlayerCount the sum of players that the game is going to manage
     * @return the id of the created players
     */
    List<Integer> register(ITronView viewObservable, IGameManager stateObserver, int managedPlayerCount);

    /**
     * Based on the player id and the direction in the steer object the, players direction is changed.
     * @param steer object with the player id and the direction
     */
    void handleSteer(Steer steer);
}
