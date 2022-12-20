package vsp.trongame.app.model.gamemanagement;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.*;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.game.IGameFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Functions as the Model's Manager. Takes controls from outside and manages the model's states, that are represented in the gui.
 */
public class GameManager implements IGameManager, ITronModel {

    private static final Map<ModelState, Map<GameState, ModelState>> transitions = new EnumMap<>(Map.of(
            ModelState.MENU, Map.of(GameState.PREPARING, ModelState.WAITING),
            ModelState.WAITING, Map.of(GameState.COUNTDOWN, ModelState.COUNTDOWN, GameState.INIT, ModelState.MENU),
            ModelState.COUNTDOWN, Map.of(GameState.RUNNING, ModelState.PLAYING, GameState.COUNTDOWN, ModelState.COUNTDOWN),
            ModelState.PLAYING, Map.of(GameState.FINISHED, ModelState.ENDING),
            ModelState.ENDING, Map.of(GameState.INIT, ModelState.MENU)));

    private final Map<Integer, IUpdateListener> listenersMap; //find listeners by their id
    private final Map<Integer, List<Integer>> listenersToPlayersMap; //map listeners to players to check for valid key input
    private final Map<Integer, Steer> managedPlayers; //managed players and their next steer event
    private ExecutorService executorService;
    private IGame game;
    private Config config;
    private boolean singleView;
    private ModelState currentState;
    private int playerCount;
    private boolean handleSteerEvents;

    public GameManager() {
        this.currentState = ModelState.MENU;
        this.handleSteerEvents = false;
        this.listenersMap = new HashMap<>();
        this.listenersToPlayersMap = new HashMap<>();
        this.managedPlayers = new HashMap<>();
    }

    @Override
    public void initialize(GameModus modus, ExecutorService executor, boolean singleView, Config config) {
        this.executorService = executor;
        this.singleView = singleView;
        this.config = config;
        this.game = IGameFactory.getGame(modus);
        game.initialize(executor, 2000, 2000, 10, 10, 10); //TODO from config

        updateListeners();
    }

    @Override
    public void handleGameState(GameState gameState) {
        ModelState newState = transitions.get(currentState).get(gameState);
        if (newState != null) transition(newState);
    }

    @Override
    public void playGame(int listenerID, int playerCount) {
        if (currentState == ModelState.MENU) {
            this.playerCount = playerCount;
            handleGameState(GameState.PREPARING);
        }
        executorService.execute(() -> {
            int managedPlayerCount = singleView ? playerCount : 1;
            game.register(this, listenersMap.get(listenerID), listenerID, managedPlayerCount);
        });
    }

    @Override
    public void registerListener(IUpdateListener listener) {
        int nextID = listenersMap.size();
        listenersMap.put(nextID, listener);
        listenersToPlayersMap.put(nextID, new ArrayList<>());
        listener.updateOnRegistration(nextID);
        listener.updateOnState(currentState.toString());
    }

    @Override
    public void handleGameTick(int tickCounter) {
        game.handleSteers(managedPlayers.values().stream().filter(Objects::nonNull).collect(Collectors.toList()), tickCounter);
    }

    @Override
    public void handleManagedPlayers(int id, Map<Integer, TronColor> managedPlayers) {

        Map<String, String> keyMapping = new HashMap<>();

        for (Map.Entry<Integer, TronColor> entry : managedPlayers.entrySet()) {
            int playerID = entry.getKey();
            this.managedPlayers.put(playerID, null);
            this.listenersToPlayersMap.computeIfAbsent(id, s -> new ArrayList<>()).add(playerID);
            String keys = config.getAttribut(String.valueOf(playerID));
            keyMapping.put(keys, entry.getValue().getColor());
        }
        listenersMap.get(id).updateOnKeyMappings(keyMapping);

    }

    @Override
    public void handleSteerEvent(int id, KeyCode key) {
        if (handleSteerEvents) {
            Steer steer = config.getSteer(key);
            int playerId = steer.getPlayerId();
            if (listenersToPlayersMap.get(id).contains(playerId)) managedPlayers.put(playerId, steer);
        }
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

        if (currentState != ModelState.PLAYING) updateListeners();
        if (currentState == ModelState.WAITING) executorService.execute(() -> game.prepare(playerCount));
        if (currentState == ModelState.PLAYING) handleSteerEvents = true;
        if (currentState == ModelState.MENU) reset();
    }

    /**
     * Updates the view depending on state.
     */
    private void updateListeners() {
        listenersMap.values().forEach(listener -> listener.updateOnState(currentState.toString()));
    }

    /**
     * Resetting data after ending state.
     */
    private void reset() {
        listenersToPlayersMap.clear();
        managedPlayers.clear();
        playerCount = 0;
    }

}
