package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.datatypes.*;
import vsp.trongame.app.model.gamemanagement.IGameData;
import vsp.trongame.app.model.gamemanagement.IGameManager;

import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Implements IGame and performs the gameLoop
 */
public class Game implements IGame, Runnable {

    private final List<IPlayer> players;
    private final Map<TronColor, List<Coordinate>> mappedPlayers; //for IGameData update
    private final List<IGameManager> stateListener;
    private final List<IGameData> dataListener;
    private final ICollisionDetector collisionDetector;

    private IArena arena;
    private int playerCount;
    private int registeredPlayerCount;
    private GameState currentState;

    public Game() {
        this.players = new ArrayList<>();
        this.stateListener = new ArrayList<>();
        this.dataListener = new ArrayList<>();
        this.mappedPlayers = new HashMap<>();
        this.collisionDetector = new CollisionDetector();
        this.currentState = GameState.INIT;
    }

    @Override
    public void prepare(int waitingTimer, int playerCount, int arenaRows, int arenaColumns) {
        this.arena = new Arena(arenaRows, arenaColumns);
        this.currentState = GameState.PREPARING;
        this.playerCount = playerCount;
        //TODO: startTimer
    }

    @Override
    public void register(IGameData dataListener, IGameManager stateListener, int managedPlayerCount) {

        if (isRegistrationAllowed(managedPlayerCount)) {
            this.dataListener.add(dataListener);

            this.stateListener.add(stateListener);
            Map<Integer, TronColor> managedPlayers = createPlayers(managedPlayerCount);
            stateListener.setManagedPlayers(managedPlayers);
        }

        if (isGameFull()) start();
    }

    @Override
    public void handleSteer(Steer steer) {
        //TODO
    }

    @Override
    public void run() {
        try {
            //TODO
            countDown();
            mockLoop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void countDown() throws InterruptedException {

        currentState = GameState.COUNTDOWN;
        for (IGameManager stateListenerEntry : stateListener) {
            stateListenerEntry.handleGameState(currentState);
        }

        for (IGameData dataListenerEntry : dataListener) {
            dataListenerEntry.updateCountDownCounter(1);
            sleep(1000);
            dataListenerEntry.updateCountDownCounter(1);
            sleep(1000);
            dataListenerEntry.updateCountDownCounter(1);
            sleep(1000);
        }
    }

    /**
     * Creates and saves the given number of players and maps their ID and Color.
     * @param count number of players to be created.
     * @return a map of the player's ID and Color.
     */
    private Map<Integer, TronColor> createPlayers(int count) {
        Map<Integer, TronColor> newPlayers = new HashMap<>();

        for (int i = 0; i < count; i++) {
            TronColor color = TronColor.getByOrdinal(registeredPlayerCount);
            IPlayer newPlayer = new Player(color, registeredPlayerCount);

            this.players.add(newPlayer);
            this.mappedPlayers.put(color, newPlayer.getCoordinates());
            newPlayers.put(registeredPlayerCount, color);

            registeredPlayerCount++;
        }
        return newPlayers;
    }

    /**
     * Checks if a registration is allowed.
     * @param playerCountToRegister the amount of players that are to be registered.
     * @return true, if registration is allowed, false otherwise.
     */
    private boolean isRegistrationAllowed(int playerCountToRegister) {
        return currentState == GameState.PREPARING && this.playerCount - this.registeredPlayerCount <= playerCountToRegister;
    }

    /**
     * Checks if the desired player count was reached.
     * @return true, if the playerCount players are registered, false otherwise.
     */
    private boolean isGameFull() {
        return this.registeredPlayerCount == playerCount;
    }

    /**
     * Makes necessary preparation for game start, then starts the game.
     */
    private void start() {

        for (IGameData dataListenerEntry : dataListener) {
            dataListenerEntry.updateArenaSize(100, 100);
        }
        //TODO: comment in when methods are ready.
        /*
        List<Coordinate> startingCoordinates = calculateFairStartingCoordinates(registeredPlayerCount);
        for (int i = 0; i < registeredPlayerCount; i++) {
            Coordinate coordinate = startingCoordinates.get(i);
            IPlayer player = players.get(i);
            player.addCoordinate(coordinate);
            player.setDirection(calculateStartingDirection(coordinate));
        }*/

        new Thread(this).start();
    }

    /**
     * Performs the game's main loop.
     */
    private void gameLoop() {
        //TODO
    }

    /**
     * Calculates in relation to the playerCount, fair starting positions for every player.
     *
     * @param playerCount how many players are playing
     * @return the list of calculated starting coordinates
     */
    private List<Coordinate> calculateFairStartingCoordinates(int playerCount) {
        return new ArrayList<>();
    }

    /**
     * Calculates for every coordinate a direction to start.
     *
     * @param coordinate is the starting coordinate of a player
     * @return the starting direction
     */
    private Direction calculateStartingDirection(Coordinate coordinate) {
        return null;
    }

    /**
     * Calculates the next coordinate based on the given direction.
     *
     * @param direction the current direction of the player
     * @return the new coordinate
     */
    private Coordinate calculateNextCoordinate(Direction direction) {
        return null;
    }

    /**
     * Checks if the game is over, if there is a winner or a draw.
     *
     * @return if the game is over
     */
    private boolean isGameOver() {
        return players.stream().filter(IPlayer::isAlive).count() <= 1;
    }

    /**
     * Finishes the running game.
     */
    private void finish() {

        //get game result
        IPlayer winner = players.stream().filter(IPlayer::isAlive).reduce((a, b) -> {
            throw new IllegalStateException("More than one Player is alive!");
        }).orElse(null);
        GameResult result =  winner == null? GameResult.DRAW : GameResult.WON;
        TronColor resultColor = winner == null? TronColor.DEFAULT : winner.getColor();

        //inform
        for (IGameData dataListenerEntry : dataListener) {
            dataListenerEntry.updateGameResult(resultColor, result);
        }

        currentState = GameState.FINISHED;
        for (IGameManager stateListenerEntry : this.stateListener) {
            stateListenerEntry.handleGameState(currentState);
        }

        reset();
    }

    /**
     * Resets the current game to its initial state.
     */
    private void reset() {
        currentState = GameState.INIT;
        playerCount = registeredPlayerCount = 0;
        stateListener.clear();
        dataListener.clear();
        arena = null;
        players.clear();
    }

    private void mockLoop() throws InterruptedException {

        //startingCoordinates
        players.get(0).getCoordinates().add(new Coordinate(1, 2));
        players.get(1).getCoordinates().add(new Coordinate(5, 5));
        dataListener.get(0).updatePlayers(mappedPlayers);

        // countdown

        this.currentState = GameState.RUNNING;
        for (IGameManager stateListenerEntry : stateListener) {
            stateListenerEntry.handleGameState(currentState);
        }

        //gameloop
        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 3));
        players.get(1).getCoordinates().add(new Coordinate(4, 5));
        dataListener.get(0).updatePlayers(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 4));
        players.get(1).getCoordinates().add(new Coordinate(3, 5));
        dataListener.get(0).updatePlayers(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 5));
        players.get(1).getCoordinates().add(new Coordinate(2, 5));
        dataListener.get(0).updatePlayers(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 6));
        dataListener.get(0).updatePlayers(mappedPlayers);

        players.get(1).crash();
    }

}
