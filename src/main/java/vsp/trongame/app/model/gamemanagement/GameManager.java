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

/**
 * Functions as the Model's Manager.
 */
public class GameManager implements IGameManager, ITronModel {

    private final IGame game;
    private final IGameData gameData;
    private final List<Integer> managedPlayers;
    private final Map<ModelState, Map<GameState, ModelState>> transitions;

    private Config config;
    private boolean singleView;
    private boolean handleSteerEvents;

    private ModelState currentState;
    private int playerCount;

    public GameManager(GameModus gameModus) {
        this.game = IGameFactory.getGame(gameModus);
        this.gameData = IGameDataFactory.getGameData(gameModus);
        this.transitions = new EnumMap<>(Map.of(
                ModelState.MENU, Map.of(GameState.PREPARING, ModelState.WAITING),
                ModelState.WAITING, Map.of(GameState.COUNTDOWN, ModelState.COUNTDOWN, GameState.INIT, ModelState.MENU),
                ModelState.COUNTDOWN, Map.of(GameState.RUNNING, ModelState.PLAYING, GameState.COUNTDOWN, ModelState.COUNTDOWN),
                ModelState.PLAYING, Map.of(GameState.FINISHED, ModelState.ENDING)));
        this.managedPlayers = new ArrayList<>();
    }

    @Override
    public void init(boolean singleView, Config config) {
        this.singleView = singleView;
        this.config = config;
        this.currentState = ModelState.MENU;
        this.handleSteerEvents = false;
        this.gameData.updateState(currentState.toString());
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
        if (handleSteerEvents) {
            Steer steer = config.getSteer(key);
            if (managedPlayers.contains(steer.getPlayerId())) game.handleSteer(steer);
        }
    }

    @Override
    public IObservableTronModel getObservableModel() {
        return (IObservableTronModel) this.gameData;
    }

    /**
     * A new state is reached and executed.
     *
     * @param gameState the game state initiating the transition.
     */
    private void transition(ModelState gameState) {
        currentState = gameState;
        gameData.updateState(currentState.toString());
        executeState();
    }

    /**
     * Executes the current state.
     */
    private void executeState() {

        switch (currentState) {

            case WAITING -> {
                //TODO: int waitingTimer = Integer.parseInt(config.getAttribut("waitingTimer"));
                Runnable task = () -> {
                    int waitingTimer = 1000;
                    game.prepare(waitingTimer, playerCount, 10, 10);
                    int managedPlayerCount = singleView ? playerCount : 1;
                    game.register(gameData, this, managedPlayerCount);
                };
                new Thread(task).start();
            }

            case PLAYING -> handleSteerEvents = true;

            case ENDING -> {
                //TODO: int endingTimer = Integer.parseInt(config.getAttribut("endingTimer"));
                int endingTimer = 1000;
                startTimer(endingTimer);
            }

            default -> {} //other states don't have behavior to execute at the moment
        }
    }

    /**
     * Transition to MENU state after Time Out.
     */
    private void finish() {
        managedPlayers.clear();
        currentState = ModelState.MENU;
        handleSteerEvents = false;
    }

    private void startTimer(int milliseconds) {
        //TODO
    }


}
