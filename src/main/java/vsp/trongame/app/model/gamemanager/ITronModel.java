package vsp.trongame.app.model.gamemanager;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

/**
 * The model which communicates with the view and the controller.
 */
public interface ITronModel {

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
}
