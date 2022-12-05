package vsp.trongame.app.model.game;

import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.gamemanagement.IGameData;
import vsp.trongame.app.model.gamemanagement.IGameManager;

/**
 * Provides funcionality for influencing a Game's state for other components such as the GameManagement component.
 */
public interface IGame {

    /**
     * Prepares the game to start, sets a timer for the countdown and creates an arena.
     * @param waitingTimer the time of the timer
     * @param playerCount the accepted playerCount
     * @param arenaRows the number of rows for the arena
     * @param arenaColumns the number of arenas for the arena
     */
    void prepare(int waitingTimer, int playerCount, int arenaRows, int arenaColumns);

    /**
     * The game saves which observers are informing the game and creates the sum of players.
     * @param viewObservable the observer of the view
     * @param stateObserver the observer of the state
     * @param managedPlayerCount the sum of players that the game is going to manage
     */
    void register(IGameData viewObservable, IGameManager stateObserver, int managedPlayerCount);

    /**
     * Based on the player id and the direction in the steer object the, players direction is changed.
     * @param steer object with the player id and the direction
     */
    void handleSteer(Steer steer);
}
