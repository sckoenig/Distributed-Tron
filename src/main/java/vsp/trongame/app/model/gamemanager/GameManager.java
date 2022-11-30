package vsp.trongame.app.model.gamemanager;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.GameState;

import java.util.Map;

/**
 * Implements the interface IGameManager
 */
public class GameManager implements IGameManager, ITronModel {

    private Config config;
    private ModelState currentState;
    private StringProperty winnerColor;
    private StringProperty gameStatus;
    private IntegerProperty counter;
    private IntegerProperty playerCount;
    private Map transitions;

    public GameManager(Config config, ITronView view, boolean singleView){
        this.config = config;
    }

    /**
     *
     * @param gameState
     */
    private void transition(GameState gameState){

    }

    /**
     * A new state is reached and executed.
     */
    private void executeState(){

    }

    @Override
    public void setWinnerColor(String winnerColor){

    }

    @Override
    public void setGameResult(String result){

    }

    @Override
    public void handleGameState(GameState gameState){

    }

    @Override
    public void setManagedPlayers(Map<Integer, Coordinate> managedPlayers) {

    }

    @Override
    public void handleSteerEvent(KeyCode key){

    }

    @Override
    public StringProperty getWinnerObservable(){
        return null;
    }

    @Override
    public StringProperty getGameResultObservable(){
        return null;
    }

    @Override
    public IntegerProperty getCounterObservable(){
        return null;
    }

    @Override
    public IntegerProperty getPlayerCountObservable(){
        return null;
    }

    @Override
    public void initializeGame(int playerNumber){

    }
}
