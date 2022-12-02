package vsp.trongame.app.model.gamemanager;

import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

/**
 * The model which communicates with the view and the controller.
 */
public interface ITronModel {

    Map<ModelState, String> MODEL_STATES = new HashMap<>(Map.of(ModelState.MENU, "menuOverlay.fxml", ModelState.WAITING, "waitingOverlay.fxml",
            ModelState.COUNTDOWN, "countdownOverlay.fxml",
            ModelState.ENDING, "endingOverlay.fxml"));

    /**
     *
     * @param key
     */
    void handleSteerEvent(KeyCode key);

    /**
     *
     * @return
     */
    StringProperty getWinnerObservable();

    /**
     *
     * @return
     */
    StringProperty getGameResultObservable();

    /**
     *
     * @return
     */
    IntegerProperty getCounterObservable();

    /**
     *
     * @return
     */
    IntegerProperty getPlayerCountObservable();

    /**
     *
     * @param playerNumber
     */
    void initializeGame(int playerNumber);

    void setView(ITronView view);
    void setConfig(Config config);
}
