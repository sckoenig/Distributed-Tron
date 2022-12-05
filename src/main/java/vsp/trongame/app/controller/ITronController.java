package vsp.trongame.app.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import vsp.trongame.app.model.ITronModel;

/**
 * Controller, which directs player input to the Model component.
 */
public interface ITronController {

    /**
     * Registers this Controller as a KeyEventHandler for the given scene.
     * @param scene given scene
     */
    void initKeyEventHandler(Scene scene);

    /**
     * When the Button "spiel starten" is clicked, the controller receives an event from the view and notifies the model.
     * @param playerCount number of players for the game
     */
    void startGame(int playerCount);

    /**
     * The keyboard input is received from the view and send to the model.
     * @param key which was pressed on the keyboard
     */
    void handleKeyEvent(KeyEvent key);

    /**
     * Sets the controller's model to which it directs input.
     * @param model model
     */
    void setModel(ITronModel model);
}
