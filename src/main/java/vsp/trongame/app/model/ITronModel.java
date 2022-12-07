package vsp.trongame.app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {

    /**
     * Initializes the Model and informs if there is one or several views.
     */
    void init();

    /**
     * Handles a steer event by accepting a KeyCode and processing it.
     * @param key keyCode of the key that initiated the steer event.
     */
    void handleSteerEvent(KeyCode key);

    /**
     * Starts a game in the Model.
     * @param playerNumber number of Players for the game.
     */
    void initializeGame(int playerNumber);

    /**
     * Returns the Model as an Observable Model with ObservableValues.
     * @return observable Model
     */
    IObservableTronModel getObservableModel();

    void finishGracefully();

    /**
     * Represents the Model with ObservableValues.
     */
    interface IObservableTronModel {

        /**
         * Returns the Model's ResultColor as an ObservableValue
         * @return game result color
         */
        StringProperty getObservableResultColor();

        /**
         * Returns the Model's ResultText as an ObservableValue
         * @return game result text
         */
        StringProperty getObservableResultText();

        /**
         * Returns the Model's Countdown Counter as an ObservableValue
         * @return game countdown counter
         */
        IntegerProperty getObserverableCountDownCounter();

        /**
         * Returns the Model's key mappings as an ObservableValue
         * @return map of key mappings
         */
        ObservableMap<String, Color> getObservableKeyMappings();


    }

}
