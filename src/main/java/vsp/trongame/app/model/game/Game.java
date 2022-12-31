package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.app.model.datatypes.*;

import java.util.*;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.*;

/**
 * Implements the game logic.
 */
public class Game implements IGame {
    private static final int ONE_SECOND = 1000;
    private final List<IPlayer> players;
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
        this.gameManagers = new HashSet<>();
        this.gameListeners = new ArrayList<>();
        this.collisionDetector = new CollisionDetector();
        this.currentState = GameState.INIT;
    }

    @Override
    public void initialize(int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService) {
        this.rows = rows;
        this.columns = columns;
        this.speed = speed;
        this.preparationTime = waitingTimer;
        this.endingTime = endingTimer;
        this.arena = new Arena(rows, columns);
        this.gameExecutor = executorService;
    }

    @Override
    public void prepare(int playerCount) {
        System.out.println("####### GAME: PREPARE CALL");
        this.playerCount = playerCount;
        transitionState(GameState.PREPARING);
    }

    @Override
    public void register(IGameManager gameManager, ITronModel.IUpdateListener gameListener, int listenerId, int managedPlayerCount) {
        System.out.println("####### GAME: REGISTER CALL");

        if (isRegistrationAllowed(managedPlayerCount)) {
            this.gameListeners.add(gameListener);
            this.gameManagers.add(gameManager);
            gameManager.handleManagedPlayers(listenerId, createPlayers(managedPlayerCount));
        }

        if (isGameFull()) transitionState(GameState.COUNTDOWN);

    }

    @Override
    public void handleSteer(Steer steer) {
        System.out.println("####### GAME: STEERS CALL -> " + steer);
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
    private void transitionState(GameState newState) {
        currentState = newState;
        executeState();
    }

    /**
     * Executes the current state.
     */
    private void executeState() {
        gameManagers.forEach(gm -> gm.handleGameState(currentState));

        switch (currentState) {
            case INIT -> reset();
            case PREPARING -> startTimer(preparationTime, currentState);
            case COUNTDOWN -> start();
            case RUNNING -> play();
            case FINISHED -> finish();
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
     * Timeout handeling deoebds on the game state the timer was started in and the current game state.
     *
     * @param startedAt the game state the timer was started in.
     */
    private void handleTimeOut(GameState startedAt) {
        if (startedAt == GameState.FINISHED && currentState == GameState.FINISHED) transitionState(GameState.INIT);
        if (startedAt == GameState.PREPARING && currentState == GameState.PREPARING) {
            if (isGameReady()) transitionState(GameState.COUNTDOWN);
            else transitionState(GameState.INIT);
        }
    }

    /**
     * Checks if enough players are registered to start the game after preperation time is over.
     *
     * @return true, if enough players are registered, false otherwise.
     */
    private boolean isGameReady() {
        return currentState == GameState.PREPARING && registeredPlayerCount >= 1;
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
        return currentState == GameState.PREPARING && this.playerCount - this.registeredPlayerCount >= playerCountToRegister;
    }

    /**
     * Creates and saves the given number of players and maps their ID and Color.
     *
     * @param count number of players to be created.
     * @return a map of the player's ID and Color.
     */
    private Map<Integer, TronColor> createPlayers(int count) {
        Map<Integer, TronColor> newPlayers = new HashMap<>();

        for (int i = 0; i < count; i++) {
            TronColor color = TronColor.getByOrdinal(registeredPlayerCount);
            IPlayer newPlayer = new Player(color, ++registeredPlayerCount);

            this.players.add(newPlayer);
            newPlayers.put(registeredPlayerCount, color);
        }
        return newPlayers;
    }

    /**
     * Makes necessary preparation for game start, then starts the game.
     */
    private void start() {
        gameListeners.forEach(gl -> gl.updateOnArena(rows, columns));

        List<Coordinate> startingCoordinates = arena.calculateFairStartingCoordinates(registeredPlayerCount);
        for (int i = 0; i < registeredPlayerCount; i++) {
            Coordinate coordinate = startingCoordinates.get(i);
            IPlayer player = players.get(i);
            player.addCoordinate(coordinate);
            player.setDirection(arena.calculateStartingDirection(coordinate));
        }

        try {
            countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!Thread.interrupted()) transitionState(GameState.RUNNING);
    }

    private void countDown() throws InterruptedException {

        for (int i = 3; i > 0; i--) {
            long startTime = System.currentTimeMillis();
            for (ITronModel.IUpdateListener listeners : gameListeners) {
                listeners.updateOnCountDown(i);
            }
            long time = System.currentTimeMillis() - startTime;
            sleep(ONE_SECOND - time);
        }
        gameListeners.forEach(ITronModel.IUpdateListener::updateOnGameStart);
    }

    /**
     * Performs the game's main loop.
     */

    private void play() {
        gameExecutor.execute(() -> {
            try {
                gameLoop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!interrupted()) transitionState(GameState.FINISHED);
        });
    }

    private void gameLoop() throws InterruptedException {

        while (!isGameOver() && !Thread.interrupted()) {
            long whileStart = System.currentTimeMillis();
            System.err.println(tickCount);

            tickCount++;
            System.err.println(players.get(0).isAlive());
            movePlayers();
            updateField();

            long timeDiff = System.currentTimeMillis() - whileStart;

            // noinspection BusyWait: tickrate here
            sleep((ONE_SECOND / speed) - timeDiff);
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
        gameExecutor.execute(() -> {
            collisionDetector.detectCollision(players, arena);
            Map<String, List<Coordinate>> map = new HashMap<>();
            for (IPlayer player : players) {
                if (player.isAlive()) map.put(player.getColor().getHex(), player.getCoordinates());
            }
            gameListeners.forEach(dl -> dl.updateOnField(map));
        });
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
    /**
     * private boolean isGameOver() {
     * return players.stream().filter(IPlayer::isAlive).count() <= 1;
     * }
     */

    private boolean isGameOver() {
        return players.stream().filter(IPlayer::isAlive).count() <= 0;
    }

    /**
     * Finishes the running game.
     */
    private void finish() {

        IPlayer winner = players.stream().filter(IPlayer::isAlive).reduce((a, b) -> {
            throw new IllegalStateException("More than one Player is alive!");
        }).orElse(null);

        GameResult result;
        TronColor resultColor;
        if (winner != null) {
            arena.deletePlayerPositions(List.of(winner.getId()));
            result = GameResult.WON;
            resultColor = winner.getColor();
        } else {
            result = GameResult.DRAW;
            resultColor = TronColor.DEFAULT;
        }

        gameListeners.forEach(listener -> listener.updateOnGameResult(resultColor.getHex(), result.getResultText()));
        startTimer(endingTime, currentState);
    }

    /**
     * Resets the current game to its initial state.
     */
    private void reset() {
        playerCount = registeredPlayerCount = tickCount = 0;
        gameManagers.clear();
        gameListeners.clear();
        players.clear();
    }

}
