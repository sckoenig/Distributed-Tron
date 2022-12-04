package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.game.IGameFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the interface IGameManager
 */
public class GameManager implements IGameManager, ITronModel {

    private final IGame game;
    private final IGameData gameData;
    private Config config;
    private boolean singleView;
    private ModelState currentState;
    private final Map<ModelState, Map<GameState, ModelState>> transitions;

    public GameManager(GameModus gameModus) {
        this.game = IGameFactory.getGame(gameModus);
        this.gameData = IGameDataFactory.getGameData(gameModus);
        this.currentState = ModelState.MENU;
        this.transitions = new HashMap<>(Map.of(
                ModelState.MENU, Map.of(GameState.PREPARING, ModelState.WAITING),
                ModelState.WAITING, Map.of(GameState.COUNTDOWN, ModelState.COUNTDOWN, GameState.INIT, ModelState.MENU),
                ModelState.COUNTDOWN, Map.of(GameState.RUNNING, ModelState.PLAYING, GameState.COUNTDOWN, ModelState.COUNTDOWN),
                ModelState.PLAYING, Map.of(GameState.FINISHED, ModelState.ENDING)));
    }

    @Override
    public void setSingleView(boolean singleView) {
        this.singleView = singleView;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public IObservableTronModel getObservableModel() {
        return (IObservableTronModel) this.gameData;
    }

    /**
     * A new state is reached and executed.
     *
     * @param gameState
     */
    private void transition(ModelState gameState) {
        currentState = gameState;
        executeState();
    }

    /**
     * Executes current state.
     */
    private void executeState() {
        System.out.println(currentState.toString());

    }

    @Override
    public void handleGameState(GameState gameState) {
        ModelState newState = transitions.get(currentState).get(gameState);
        if (newState != null) transition(newState);
    }

    @Override
    public void handleSteerEvent(KeyCode key) {
        if (currentState == ModelState.PLAYING) {
            //game handle
        }
    }

    @Override
    public void initializeGame(int playerNumber) {
        handleGameState(GameState.PREPARING);
    }


    @Override
    public void setManagedPlayers(Map<Integer, Coordinate> managedPlayers) {
        //new map
        // iterate over managedPlayers
        // get key config for every player
        // add keyConfig, Coord to new map
        // add to observablePlayers
    }



}
