package vsp.trongame.application.model;

import vsp.trongame.Modus;
import vsp.trongame.application.model.gamemanagement.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {

    /**
     * Handles a steer event by accepting a key String and processing it.
     *
     * @param id the initiator's registration id
     * @param key the key that initiated the steer event.
     */
    void handleSteerEvent(int id, String key);

    /**
     * Initializes the Model with necessary dependencies.
     */
    void initialize(Configuration config, Modus modus, boolean singleView, ExecutorService executorService);

    /**
     * Registers the initiator at the game.
     *
     * @param listener the initiator's listener
     * @param playerNumber number of Players for the game.
     */
    void playGame(IUpdateListener listener, int playerNumber);

}
