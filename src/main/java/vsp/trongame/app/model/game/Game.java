package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import vsp.trongame.app.model.datatypes.Direction;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.gamemanager.IGameManager;

import java.util.List;

/**
 * Implements IGame and creates the gameLoop
 */
public class Game implements IGame, Runnable{

    private List<IPlayer> players;
    private IArena arena;
    private ICollisionDetector collisionDetector;

    public Game(int speed, int rows, int columns){

    }

    private void gameLoop(){

    }

    /**
     * Calculates in relation to the playerCount, fair starting positions for every player.
     * @param playerCount how many players are playing
     * @return the list of calculated starting coordinates
     */
    private List<Coordinate> calculateFairStartingCoordinate(int playerCount){
        return null;
    }

    /**
     * Calculates for every coordinate a direction to start.
     * @param coordinate is the starting coordinate of a player
     * @return the starting direction
     */
    private Direction calculateStartingDirection(Coordinate coordinate){
        return null;
    }

    /**
     * Calculates the next coordinate based on the given direction.
     * @param direction the current direction of the player
     * @return the new coordinate
     */
    private Coordinate calculateNextCoordinate(Direction direction){
        return null;
    }

    /**
     * Sets the actualPlayerCount to the sum of players who joined the game during the waiting screen.
     * @param ActualPlayerCount the sum of players who joined
     */
    private void setActualPlayerCount(int ActualPlayerCount){

    }

    /**
     * Checks if the game is over, if there is a winner or a draw.
     * @return if the game is over
     */
    private boolean isGameOver(){
        return false;
    }

    @Override
    public void prepare(int waitingTimer, int playerCount){

    }

    @Override
    public List<Integer> register(ITronView viewObservable, IGameManager stateObserver, int managedPlayerCount) {
        return null;
    }

    @Override
    public void handleSteer(Steer steer) {

    }

    @Override
    public void run() {

    }
}
