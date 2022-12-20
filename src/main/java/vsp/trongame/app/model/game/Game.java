package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.*;
import vsp.trongame.app.model.gamemanagement.IGameManager;

import java.util.*;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.*;

/**
 * Implements IGame and performs the gameLoop
 */
public class Game implements IGame {

    private final List<IPlayer> players;
    private final Map<String, List<Coordinate>> mappedPlayers; //for gameLoop update
    private final Set<IGameManager> gameManagers; //listeners be related to the same manager
    private final List<ITronModel.IUpdateListener> gameListeners;
    private final ICollisionDetector collisionDetector;
    private ExecutorService gameExecutor;
    private int speed;
    private int preparationTime;
    private int endingTime;
    private IArena arena;
    private int rows;
    private int columns;
    private int playerCount;
    private int registeredPlayerCount;
    private GameState currentState;
    private int tickCount;

    public Game() {
        this.players = new ArrayList<>();
        this.gameManagers= new HashSet<>();
        this.gameListeners = new ArrayList<>();
        this.mappedPlayers = new HashMap<>();
        this.collisionDetector = new CollisionDetector();
        this.currentState = GameState.INIT;
    }

    @Override
    public void initialize(ExecutorService executorService, int waitingTimer, int endingTime, int rows, int columns, int speed) {
        this.gameExecutor = executorService;
        this.rows = rows;
        this.columns = columns;
        this.speed = speed;
        this.preparationTime = waitingTimer;
        this.endingTime = endingTime;
        this.arena = new Arena(rows, columns);
    }

    @Override
    public void prepare(int playerCount) {
        this.playerCount = playerCount;
        transitionState(GameState.PREPARING);
    }

    @Override
    public void register(IGameManager gameManager, ITronModel.IUpdateListener gameListener, int listenerId, int managedPlayerCount) {

        if (isRegistrationAllowed(managedPlayerCount)) {
            this.gameListeners.add(gameListener);
            this.gameManagers.add(gameManager);
            gameManager.handleManagedPlayers(listenerId, createPlayers(managedPlayerCount));
        }

        if (isGameFull()) transitionState(GameState.COUNTDOWN);

    }

    @Override
    public void handleSteers(int tickCounter, List<Steer> steers) {
        //TODO
    }

    /**
     * Transitions to the next state and informs stateListeners.
     */
    private void transitionState(GameState newState) {
        currentState = newState;
        executeState();
    }

    private void executeState() {

        gameManagers.forEach(gm -> gm.handleGameState(currentState));

        switch (currentState) {
            case INIT -> reset();
            case PREPARING -> startTimer(preparationTime);
            case COUNTDOWN -> start();
            case RUNNING -> play();
            case FINISHED -> finish();
        }
    }

    private void startTimer(int waitingTime) {
        gameExecutor.execute(() -> {
            try {
                sleep(waitingTime);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            if (!currentThread().isInterrupted()) handleTimeOut();
        });
    }

    private void handleTimeOut() {
        if (currentState == GameState.FINISHED) transitionState(GameState.INIT);
        if (currentState == GameState.PREPARING) {
            if (isGameReady()) transitionState(GameState.COUNTDOWN);
            else transitionState(GameState.INIT);
        }
    }

    /**
     * Checks if enough players are registered to start the game after preperation time is over.
     * @return true, if enough players are registered, false otherwise.
     */
    private boolean isGameReady() {
        return currentState == GameState.PREPARING && registeredPlayerCount > 2;
    }

    /**
     * Checks if the desired player count was reached.
     * @return true, if the playerCount players are registered, false otherwise.
     */
    private boolean isGameFull() {
        return this.registeredPlayerCount == playerCount;
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
            this.mappedPlayers.put(color.getColor(), newPlayer.getCoordinates());
            newPlayers.put(registeredPlayerCount, color);

            registeredPlayerCount++;
        }
        return newPlayers;
    }

    /**
     * Makes necessary preparation for game start, then starts the game.
     */
    private void start() {

        gameListeners.forEach(gl -> gl.updateOnArena(100,100));

        /*
        List<Coordinate> startingCoordinates = calculateFairStartingCoordinates(registeredPlayerCount);
        for (int i = 0; i < registeredPlayerCount; i++) {
            Coordinate coordinate = startingCoordinates.get(i);
            IPlayer player = players.get(i);
            player.addCoordinate(coordinate);
            player.setDirection(calculateStartingDirection(coordinate));
        }

         */

        try {
            countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!Thread.interrupted()) transitionState(GameState.RUNNING);
    }

    private void countDown() throws InterruptedException {

        for (ITronModel.IUpdateListener gl : gameListeners) {
            for (int i = 3; i > 0; i--) {
                gl.updateOnCountDown(i);
                sleep(1000);
            }
        }
        gameListeners.forEach(ITronModel.IUpdateListener::updateOnGameStart);

    }

    /**
     * Performs the game's main loop.
     */

    private void play() {
        gameExecutor.execute(() -> {
            try {
                //gameLoop();
                mockLoop();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            if (!interrupted()) transitionState(GameState.FINISHED);
        });
    }

    private void gameLoop() throws InterruptedException {
        while (!isGameOver() && !Thread.interrupted()){
            players.forEach(p -> {
                if(p.isAlive()){
                    //Coordinate nextCoordinate = calculateNextCoordinate(p.getHeadPosition(), p.performDirectionChange());
                    //p.addCoordinate(nextCoordinate);
                }
            });
            collisionDetector.detectCollision(players, arena);

            tickCount++;
            gameListeners.forEach(dl -> dl.updateOnField(mappedPlayers));
            gameManagers.forEach(gm -> gm.handleGameTick(tickCount));

            // noinspection BusyWait: tickrate here
            sleep(0);
        }
    }

    //TODO move to arena

    /**
     * Calculates in relation to the playerCount, fair starting positions for every player.
     *
     * @param playerCount how many players are playing
     * @return the list of calculated starting coordinates
     */
    private List<Coordinate> calculateFairStartingCoordinates(int playerCount) {
        List<Coordinate> fairPositions = new ArrayList<>();
        switch (playerCount) {
            case 2 -> {
                fairPositions.add(new Coordinate(0,0));
                fairPositions.add(new Coordinate(rows, columns));
            }
            case 3 -> {
                fairPositions.add(new Coordinate(0, rows));
                fairPositions.add(new Coordinate(rows, columns));
                fairPositions.add(new Coordinate(rows, 0));
            }
            case 4 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(rows, columns));
                fairPositions.add(new Coordinate(0, columns));
                fairPositions.add(new Coordinate(rows, 0));
            }
            case 5 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(rows, columns));
                fairPositions.add(new Coordinate(0, columns));
                fairPositions.add(new Coordinate(rows, 0));
                fairPositions.add(new Coordinate(rows / 2, columns / 2));
            }
            case 6 -> {
                fairPositions.add(new Coordinate(0, 0));
                fairPositions.add(new Coordinate(rows, columns));
                fairPositions.add(new Coordinate(0, columns));
                fairPositions.add(new Coordinate(rows, 0));
                fairPositions.add(new Coordinate(rows / 2, 0));
                fairPositions.add(new Coordinate(rows / 2, columns));
            }
            default -> {
            }
        }
        return fairPositions;
    }

    //TODO move to arena

    /**
     * Calculates for every coordinate a direction to start.
     *
     * @param coordinate is the starting coordinate of a player
     * @return the starting direction
     */
    private Direction calculateStartingDirection(Coordinate coordinate) {
        if(coordinate.x == 0){
            return Direction.RIGHT;
        }else if(coordinate.x == columns){
            return Direction.LEFT;
        } else{
            return Direction.DOWN;
        }
    }

    /**
     * Calculates the next coordinate based on the given direction.
     *
     * @param direction the current direction of the player
     * @return the new coordinate
     */
    private Coordinate calculateNextCoordinate(Coordinate coordinate, Direction direction) {
        switch (direction){
            case DOWN -> {
                return new Coordinate(coordinate.x, coordinate.y+1);
            }
            case UP -> {
                return new Coordinate(coordinate.x, coordinate.y-1);
            }
            case LEFT -> {
                return new Coordinate(coordinate.x-1, coordinate.y);
            }
            case RIGHT -> {
                return new Coordinate(coordinate.x+1, coordinate.y);
            }
            default -> {
                return coordinate;
            }
        }
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
        GameResult result = winner == null ? GameResult.DRAW : GameResult.WON;
        TronColor resultColor = winner == null ? TronColor.DEFAULT : winner.getColor();

        //inform
        gameListeners.forEach(gameData -> gameData.updateOnGameResult(resultColor.getColor(), result.getResultText()));

        startTimer(endingTime);
    }

    /**
     * Resets the current game to its initial state.
     */
    private void reset() {
        playerCount = registeredPlayerCount = tickCount = 0;
        gameManagers.clear();
        gameListeners.clear();
        mappedPlayers.clear();
        players.clear();
    }

    private void mockLoop() throws InterruptedException {

        //startingCoordinates
        players.get(0).getCoordinates().add(new Coordinate(1, 2));
        players.get(1).getCoordinates().add(new Coordinate(5, 5));
        gameListeners.get(0).updateOnField(mappedPlayers);

        //gameloop
        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 3));
        players.get(1).getCoordinates().add(new Coordinate(4, 5));
        gameListeners.get(0).updateOnField(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 4));
        players.get(1).getCoordinates().add(new Coordinate(3, 5));
        gameListeners.get(0).updateOnField(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 5));
        players.get(1).getCoordinates().add(new Coordinate(2, 5));
        gameListeners.get(0).updateOnField(mappedPlayers);

        sleep(500);

        players.get(0).getCoordinates().add(new Coordinate(1, 6));
        gameListeners.get(0).updateOnField(mappedPlayers);

        players.get(1).crash();
    }

}
