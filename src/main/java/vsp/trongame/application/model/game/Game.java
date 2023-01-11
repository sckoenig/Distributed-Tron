package vsp.trongame.application.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.Modus;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.gamemanagement.IGameManager;
import vsp.trongame.application.model.datatypes.*;

import java.util.*;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.*;

/**
 * Implements the game logic.
 */
public class Game implements IGame {
    private static final int ONE_SECOND = 1000;
    private static final int COUNTDOWN_LENGTH = 3;
    private final List<IPlayer> players;
    private final Set<IGameManager> gameManagers; //listeners may be related to the same manager
    private final List<IUpdateListener> updateListeners;
    private final ICollisionDetector collisionDetector;
    private ExecutorService gameExecutor;
    private int speed;
    private int registrationTime;
    private int finishingTime;
    private IArena arena;
    private int playerCount;
    private int registeredPlayerCount;
    private GameState currentState;

    public Game() {
        this.players = new ArrayList<>();
        this.gameManagers = new HashSet<>();
        this.updateListeners = new ArrayList<>();
        this.collisionDetector = new CollisionDetector();
        this.currentState = GameState.INIT;
    }

    @Override
    public void initialize(Modus modus, int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService) {
        this.speed = speed;
        this.registrationTime = waitingTimer;
        this.finishingTime = endingTimer;
        this.arena = IArenaFactory.getArena(modus, rows, columns);
        this.gameExecutor = executorService;
    }

    @Override
    public void prepareForRegistration(int playerCount) {
        if (currentState == GameState.INIT) {
            this.playerCount = playerCount;
            transitionState(GameState.REGISTRATION);
        }
    }

    @Override
    public void register(IGameManager gameManager, IUpdateListener gameListener, int listenerId, int managedPlayerCount) {
        if (isRegistrationAllowed(managedPlayerCount)) {
            if (gameListener != null ) this.updateListeners.add(gameListener);
            this.gameManagers.add(gameManager);
            gameManager.handleManagedPlayers(listenerId, createPlayers(managedPlayerCount));

            if (isGameFull()) {
                transitionState(GameState.STARTING);
            }
        } else {
            gameManager.handleGameState(GameState.INIT); // registration denied
        }
    }

    @Override
    public void handleSteer(Steer steer) {
        int playerId = steer.playerId();
        DirectionChange directionChange = steer.directionChange();
        for (IPlayer player : players) {
            if (player.getId() == playerId) {
                player.setNextDirectionChange(directionChange);
                break;
            }
        }
    }

    /**
     * Transitions to the next state and informs stateListeners.
     */
    private synchronized void transitionState(GameState newState) {
        // synchronized: may be used by timer-thread *and* main-thread
        if (currentState != newState) {
            currentState = newState;
            executeState();
        }
    }

    /**
     * Executes the current state.
     */
    private void executeState() {
        gameManagers.forEach(gm -> gm.handleGameState(currentState));

        switch (currentState) {
            case INIT -> resetGame();
            case REGISTRATION -> startTimer(registrationTime, currentState);
            case STARTING -> startGame();
            case RUNNING -> runGame();
            case FINISHING -> finishGame();
        }
    }

    /**
     * Starts a timer.
     *
     * @param waitingTime the time to wait
     * @param startedAt   the game state the timer is started in.
     */
    private void startTimer(int waitingTime, GameState startedAt) {
        gameExecutor.execute(() -> {
            try {
                sleep(waitingTime);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            if (!currentThread().isInterrupted()) handleTimeOut(startedAt);
        });
    }

    /**
     * Timeout handeling on the game state the timer was started in and the current game state.
     *
     * @param startedAt the game state the timer was started in.
     */
    private void handleTimeOut(GameState startedAt) {
        if (startedAt == GameState.FINISHING && currentState == GameState.FINISHING) transitionState(GameState.INIT);
        if (startedAt == GameState.REGISTRATION && currentState == GameState.REGISTRATION) {
            if (isGameReady()) {
                transitionState(GameState.STARTING);
            }
            else transitionState(GameState.INIT);
        }
    }

    /**
     * Checks if enough players are registered to start the game after preperation time is over.
     *
     * @return true, if enough players are registered, false otherwise.
     */
    private boolean isGameReady() {
        return currentState == GameState.REGISTRATION && registeredPlayerCount >= 2;
    }

    /**
     * Checks if the desired player count was reached.
     *
     * @return true, if the playerCount players are registered, false otherwise.
     */
    private boolean isGameFull() {
        return this.registeredPlayerCount == playerCount;
    }

    /**
     * Checks if a registration is allowed.
     *
     * @param playerCountToRegister the amount of players that are to be registered.
     * @return true, if registration is allowed, false otherwise.
     */
    private boolean isRegistrationAllowed(int playerCountToRegister) {
        return currentState == GameState.REGISTRATION && this.playerCount - this.registeredPlayerCount >= playerCountToRegister;
    }

    /**
     * Creates and saves the given number of players and maps their ID and Color.
     *
     * @param count number of players to be created.
     * @return a list of the player's ID´s
     */
    private List<Integer> createPlayers(int count) {
        List<Integer>  newPlayers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            IPlayer newPlayer = new Player(registeredPlayerCount);

            this.players.add(newPlayer);
            newPlayers.add(registeredPlayerCount);
            registeredPlayerCount++;
        }
        return newPlayers;
    }

    /**
     * Makes necessary preparation for game start, then starts the game.
     */
    private void startGame() {
        List<Coordinate> startingCoordinates = arena.calculateFairStartingCoordinates(registeredPlayerCount);
        for (int i = 0; i < registeredPlayerCount; i++) {
            Coordinate coordinate = startingCoordinates.get(i);
            IPlayer player = players.get(i);
            player.addCoordinate(coordinate);
            player.setDirection(arena.calculateStartingDirection(coordinate));
        }
        updateListeners.forEach(gl -> gl.updateOnArena(arena.getRows(), arena.getColumns()));
        updateListeners.forEach(gl -> gl.updateOnField(updatePlayerMap()));
        transitionState(GameState.RUNNING);
    }

    private void countDown() throws InterruptedException {

        for (int i = COUNTDOWN_LENGTH; i > 0; i--) {
            long startTime = System.currentTimeMillis();
            for (IUpdateListener listeners : updateListeners) {
                listeners.updateOnCountDown(i);
            }
            long time = System.currentTimeMillis() - startTime;
            sleep(ONE_SECOND - time);
        }
        updateListeners.forEach(IUpdateListener::updateOnGameStart);
    }

    /**
     * Performs the game's main loop.
     */

    private void runGame() {
        gameExecutor.execute(() -> {
            try {
                countDown();
                gameLoop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!interrupted()) transitionState(GameState.FINISHING);
        });
    }

    private void gameLoop() throws InterruptedException {

        while (!isGameOver() && !Thread.interrupted()) {

            long whileStart = System.currentTimeMillis();
            movePlayers();
            updateField();
            long timeDiff = System.currentTimeMillis() - whileStart;

            long sleepTime = (ONE_SECOND / speed) - timeDiff;
            if (sleepTime >= 0) {
                // noinspection BusyWait: tickrate here
                sleep(sleepTime);
            }
        }
    }

    private void movePlayers() {
        players.forEach(p -> {
            if (p.isAlive()) {
                Coordinate nextCoordinate = calculateNextCoordinate(p.getHeadPosition(), p.performDirectionChange());
                p.addCoordinate(nextCoordinate);
            }
        });
    }

    private void updateField() {
        collisionDetector.detectCollision(players, arena);
        updateListeners.forEach(dl -> dl.updateOnField(updatePlayerMap()));
    }

    /**
     * Creates a map matching the map needed for the update listener.
     * @return map of players
     */
    private Map<Integer, List<Coordinate>> updatePlayerMap(){
        Map<Integer, List<Coordinate>> map = new HashMap<>();
        for (IPlayer player : players) {
            if (player.isAlive()) map.put(player.getId(), new ArrayList<>(player.getCoordinates()));
        }
        return map;
    }


    /**
     * Calculates the next coordinate based on the given direction.
     *
     * @param direction the current direction of the player
     * @return the new coordinate
     */
    private Coordinate calculateNextCoordinate(Coordinate coordinate, Direction direction) {
        return switch (direction) {
            case DOWN -> new Coordinate(coordinate.x, coordinate.y + 1);
            case UP -> new Coordinate(coordinate.x, coordinate.y - 1);
            case LEFT -> new Coordinate(coordinate.x - 1, coordinate.y);
            case RIGHT -> new Coordinate(coordinate.x + 1, coordinate.y);
        };
    }

    /**
     * Checks if the game is over, if there is a winner or a draw.
     *
     * @return if the game is over
     */
    private boolean isGameOver() {
        return players.stream().filter(IPlayer::isAlive).count() < 2;
    }

    /**
     * Finishes the running game.
     */
    private void finishGame() {

        IPlayer winner = players.stream().filter(IPlayer::isAlive).reduce((a, b) -> {
            throw new IllegalStateException("More than one Player is alive!");
        }).orElse(null);

        GameResult result;
        int winnerId;
        if (winner != null) {
            arena.deletePlayerPositions(List.of(winner.getId()));
            result = GameResult.WON;
            winnerId = winner.getId();
        } else {
            result = GameResult.DRAW;
            winnerId = -1;
        }

        updateListeners.forEach(listener -> listener.updateOnGameResult(winnerId, result.getResultText()));
        startTimer(finishingTime, currentState);
    }

    /**
     * Resets the current game to its initial state.
     */
    private void resetGame() {
        playerCount = registeredPlayerCount = 0;
        gameManagers.clear();
        updateListeners.clear();
        players.clear();
    }

}
