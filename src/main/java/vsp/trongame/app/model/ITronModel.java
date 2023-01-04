package vsp.trongame.app.model;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanagement.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {

    /**
     * Handles a steer event by accepting a KeyCode and processing it.
     *
     * @param id the initiator's registration id
     * @param key the key that initiated the steer event.
     */
    void handleSteerEvent(int id, String key);

    /**
     * Initializes the Model with necessary dependencies.
     */
    void initialize(Configuration config, GameModus modus, boolean singleView, ExecutorService executorService);

    /**
     * Registers the initiator at the game.
     *
     * @param id the initiator's registration id
     * @param playerNumber number of Players for the game.
     */
    void playGame(int id, int playerNumber);

    /**
     * Registers a Listener at the model for updates.
     * @param listener the listener.
     */
    void registerUpdateListener(IUpdateListener listener);


}
