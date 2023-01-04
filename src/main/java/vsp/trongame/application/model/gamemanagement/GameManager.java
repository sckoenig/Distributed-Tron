package vsp.trongame.application.model.gamemanagement;

import vsp.trongame.Modus;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.datatypes.*;
import vsp.trongame.application.model.ITronModel;
import vsp.trongame.application.model.game.IGame;
import vsp.trongame.application.model.game.IGameFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Functions as the Model's Manager. Takes controls from outside and manages the model's states, that are represented in the gui.
 */
public class GameManager implements IGameManager, ITronModel {

    private static final Map<ModelState, Map<GameState, ModelState>> TRANSITIONS = new EnumMap<>(Map.of(
            ModelState.MENU, Map.of(GameState.REGISTRATION, ModelState.WAITING),
            ModelState.WAITING, Map.of(GameState.STARTING, ModelState.COUNTDOWN, GameState.INIT, ModelState.MENU),
            ModelState.COUNTDOWN, Map.of(GameState.RUNNING, ModelState.PLAYING, GameState.STARTING, ModelState.COUNTDOWN),
            ModelState.PLAYING, Map.of(GameState.FINISHING, ModelState.ENDING),
            ModelState.ENDING, Map.of(GameState.INIT, ModelState.MENU)));

    private final Map<Integer, IUpdateListener> listenersMap; //find listeners by their id
    private final Map<Integer, List<Integer>> listenersToPlayersMap; //map listeners to players to check for valid key input
    private Configuration config;
    private ExecutorService executorService;
    private IGame game;
    private boolean singleView;
    private ModelState currentState;
    private boolean handleGameEvents;

    public GameManager() {
        this.currentState = ModelState.MENU;
        this.handleGameEvents = false;
        this.listenersMap = new HashMap<>();
        this.listenersToPlayersMap = new HashMap<>();
    }

    @Override
    public void initialize(Configuration config, Modus modus, boolean singleView, ExecutorService executorService) {
        this.config = config;
        this.singleView = singleView;
        this.game = IGameFactory.getGame(modus);
        this.executorService = executorService;
        game.initialize(modus, Integer.parseInt(config.getAttribut(Configuration.SPEED)),
                Integer.parseInt(config.getAttribut(Configuration.ROWS)),
                Integer.parseInt(config.getAttribut(Configuration.COLUMNS)),
                Integer.parseInt(config.getAttribut(Configuration.WAITING_TIMER)),
                Integer.parseInt(config.getAttribut(Configuration.ENDING_TIMER)), executorService);

        updateListeners();
    }

    @Override
    public void handleGameState(GameState gameState) {
        ModelState newState = TRANSITIONS.get(currentState).get(gameState);
        if (newState != null) transition(newState);
    }

    @Override
    public void playGame(int listenerID, int playerCount) {
        if (currentState == ModelState.MENU) {

            game.prepareForRegistration(playerCount);
            executorService.execute(() -> {
                int managedPlayerCount = singleView ? playerCount : 1;
                if (listenersMap.containsKey(listenerID))
                    listenersMap.values().forEach(listener ->  game.register(this, listenersMap.get(listenerID), listenerID, managedPlayerCount));
            });
        }
    }

    @Override
    public void registerUpdateListener(IUpdateListener listener) {
        int nextID = listenersMap.size();
        listenersMap.put(nextID, listener);
        listener.updateOnRegistration(nextID);
        listener.updateOnState(currentState.toString());
    }

    @Override
    public void handleManagedPlayers(int id, List<Integer> managedPlayers) {
        Map<String, Integer> keyMapping = new HashMap<>();

        for (Integer playerID : managedPlayers) {
            this.listenersToPlayersMap.computeIfAbsent(id, s -> new ArrayList<>()).add(playerID);
            String keys = config.getKeyMappingForPlayer(playerID);
            keyMapping.put(keys, playerID);
        }
        listenersMap.get(id).updateOnKeyMappings(keyMapping);
    }

    @Override
    public void handleSteerEvent(int id, String key) {
        if (handleGameEvents) {
            Steer steer = config.getSteer(key);
            if(steer != null) {
                int playerId = steer.playerId();
                if (listenersToPlayersMap.get(id).contains(playerId)) { // is steer allowed
                    game.handleSteer(steer);
                }
            }
        }
    }

    /**
     * A new state is reached and executed.
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
        if (currentState == ModelState.PLAYING) handleGameEvents = true;
        if (currentState == ModelState.MENU) reset();
    }

    /**
     * Updates listeners about state updates.
     */
    private void updateListeners() {
        listenersMap.values().forEach(listener -> listener.updateOnState(currentState.toString()));
    }

    /**
     * Resetting management data after ending state.
     */
    private void reset() {
        handleGameEvents = false;
        listenersToPlayersMap.clear();
    }

}
