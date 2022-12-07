package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.game.IGameFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Functions as the Model's Manager.
 */
public class GameManager implements IGameManager, ITronModel {

    private final ExecutorService executorService;
    private final ITronView view;
    private final IGame game;
    private final IGameData gameData;
    private final List<Integer> managedPlayers;
    private final Map<ModelState, Map<GameState, ModelState>> transitions;
    private final Config config;
    private final boolean singleView;

    private ModelState currentState;
    private int playerCount;

    public GameManager(GameModus gameModus, ITronView tronView, boolean singleView, Config config) {
        this.view = tronView;
        this.singleView = singleView;
        this.config = config;

        this.executorService = Executors.newSingleThreadExecutor();
        this.game = IGameFactory.getGame(gameModus, executorService, 2000, 10, 10, 10); //TODO read from config
        this.gameData = IGameDataFactory.getGameData(gameModus, tronView);
        this.transitions = new EnumMap<>(Map.of(
                ModelState.MENU, Map.of(GameState.PREPARING, ModelState.WAITING),
                ModelState.WAITING, Map.of(GameState.COUNTDOWN, ModelState.COUNTDOWN, GameState.INIT, ModelState.MENU),
                ModelState.COUNTDOWN, Map.of(GameState.RUNNING, ModelState.PLAYING, GameState.COUNTDOWN, ModelState.COUNTDOWN),
                ModelState.PLAYING, Map.of(GameState.FINISHED, ModelState.ENDING)));
        this.managedPlayers = new ArrayList<>();
    }

    @Override
    public void init() {
        this.currentState = ModelState.MENU;
        updateView();
    }

    @Override
    public void handleGameState(GameState gameState) {
        ModelState newState = transitions.get(currentState).get(gameState);
        if (newState != null) transition(newState);
    }

    @Override
    public void initializeGame(int playerNumber) {
        this.playerCount = playerNumber;
        handleGameState(GameState.PREPARING);
    }

    @Override
    public void setManagedPlayers(Map<Integer, TronColor> managedPlayers) {

        Map<String, TronColor> keyMapping = new HashMap<>();

        for (Map.Entry<Integer, TronColor> entry : managedPlayers.entrySet()) {
            int playerID = entry.getKey();
            this.managedPlayers.add(playerID);
            String keys = config.getAttribut(String.valueOf(playerID));
            keyMapping.put(keys, entry.getValue());
        }

        gameData.updateKeyMappings(keyMapping);
    }

    @Override
    public void handleSteerEvent(KeyCode key) {
        if (currentState == ModelState.PLAYING) {
            Steer steer = config.getSteer(key);
            if (managedPlayers.contains(steer.getPlayerId())) game.handleSteer(steer);
        }
    }

    @Override
    public IObservableTronModel getObservableModel() {
        return (IObservableTronModel) this.gameData;
    }

    @Override
    public void finishGracefully() {
        executorService.shutdownNow();
    }

    /**
     * A new state is reached and executed.
     *
     * @param newState the game state initiating the transition.
     */
    private void transition(ModelState newState) {
        currentState = newState;
        executeState();
    }

    /**
     * Executes the current state.
     */
    private void executeState() {
        updateView();

        if (currentState == ModelState.WAITING) executorService.execute(() -> {
            game.prepare(playerCount);
            int managedPlayerCount = singleView ? playerCount : 1;
            game.register(gameData, this, managedPlayerCount);
        });

        if (currentState == ModelState.ENDING) endingTimer(2000); //TODO read timer from config
    }

    /**
     * Updates the view depending on state.
     */
    private void updateView() {
        view.hideOverlays();
        if (currentState != ModelState.PLAYING) view.showOverlay(currentState.toString());
        if (currentState == ModelState.MENU) view.clear();
    }

    /**
     * Transition to MENU state after Time Out.
     */
    private void finish() {
        managedPlayers.clear();
        currentState = ModelState.MENU;
        updateView();
        gameData.updateCountDownCounter(0);
    }

    /**
     * Timer for ending state.
     * @param milliseconds time of the ending state.
     */
    private void endingTimer(int milliseconds) {
        executorService.execute(() -> {
            try{
                sleep(milliseconds);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            if (!currentThread().isInterrupted()) finish();
        });
    }

}
