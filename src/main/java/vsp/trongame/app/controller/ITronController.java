package vsp.trongame.app.controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.gamemanager.ITronModel;

/**
 * The controller which communicates with the model and the view.
 */
public interface ITronController {

    /**
     *
     * @param scene
     */
    void initKeyEventHandler(Scene scene);

    /**
     * When the Button "spiel starten" is clicked, the controller receives an event from the view and notifies the model.
     * @param event that the game was started
     */
    void startGame(ActionEvent event);

    /**
     * The keyboard input is received from the view and send to the model.
     * @param key which was pressed on the keyboard
     */
    void handleKeyEvent(KeyEvent key);

    /**
     * Gets the TronModel which works with TronController.
     * @return the TronModel which works with the controller
     */
    ITronModel getModel();

    void setModel(ITronModel model);
}
