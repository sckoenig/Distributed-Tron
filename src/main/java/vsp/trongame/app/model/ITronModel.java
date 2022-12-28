package vsp.trongame.app.model;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanagement.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {


    /**
     * Handles a steer event by accepting a KeyCode and processing it.
     *
     * @param id the initiator's registration id
     * @param key keyCode of the key that initiated the steer event.
     */
    void handleSteerEvent(int id, KeyCode key);

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


    /**
     * Represents a listener the model can inform about its state.
     */
    interface IUpdateListener {

        /**
         * After Registration, the listener is informed about its id.
         * @param id the id under which the listener is known by the model
         */
        void updateOnRegistration(int id);

        /**
         * Informs the Listener about Key Mappings.
         * @param mappings mappings in the form of keys, color in hex
         */
        void updateOnKeyMappings(Map<String, String> mappings);

        /**
         * Informs the Listener about the arena's size.
         * @param rows number of rows
         * @param columns number of columns
         */
        void updateOnArena(int rows, int columns);

        /**
         * Informs the Listener about the model's state.
         * @param state model's current state
         */
        void updateOnState(String state);

        /**
         * Informs the Listener about the game's start.
         */
        void updateOnGameStart();

        /**
         * Informs the Listener about the result of a finished game.
         * @param color the winner's color or default color on draw
         * @param result the result text
         */
        void updateOnGameResult(String color, String result);

        /**
         * Informs the Listener about the countdown's value.
         * @param value countdown value
         */
        void updateOnCountDown(int value);

        /**
         * Informs the Listener about the field's status.
         * @param field the field in the form of color and list of coordinates
         */
        void updateOnField(Map<String, List<Coordinate>> field);

    }



}
