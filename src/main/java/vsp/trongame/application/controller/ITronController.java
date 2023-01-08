package vsp.trongame.application.controller;

import javafx.scene.input.KeyEvent;
import vsp.trongame.application.model.ITronModel;
import vsp.trongame.application.model.IUpdateListener;

/**
 * Controller, which directs user input to the Model component.
 */
public interface ITronController {

    /**
     * When the Button "PLAY" is clicked, the controller receives an event from the view and notifies the model.
     * @param listener the initiator's listener
     * @param playerCount number of players for the game
     */
    void playGame(IUpdateListener listener, int playerCount);

    /**
     * The keyboard input is received from the view and send to the model.
     * @param id the initiator's registartion id
     * @param key which was pressed on the keyboard
     */
    void handleKeyEvent(int id, KeyEvent key);

    /**
     * Sets the controller's model to which it directs input.
     * @param model model
     */
    void initialize(ITronModel model);
}
